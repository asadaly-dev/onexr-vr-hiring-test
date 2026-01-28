package com.example.onexr_vr_app
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Handler
import android.os.Looper
import android.view.Surface
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import androidx.media3.common.Player

class VrSphereRenderer(private val context: Context) :
    GLSurfaceView.Renderer, SensorEventListener {

    private val sphere = Sphere()

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val rotationMatrix = FloatArray(16)

    private var textureId = 0
    private lateinit var surfaceTexture: SurfaceTexture
    private var player: ExoPlayer? = null

    private val mainHandler = Handler(Looper.getMainLooper())

    private var program = 0

    private val projection = FloatArray(16)
    private val view = FloatArray(16)
    private val mvp = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        Matrix.setIdentityM(rotationMatrix, 0)

        textureId = createOESTexture()
        surfaceTexture = SurfaceTexture(textureId)

        mainHandler.post {
            player = ExoPlayer.Builder(context).build().apply {
                setVideoSurface(Surface(surfaceTexture))
                setMediaItem(MediaItem.fromUri("asset:///experience_360.mp4"))
                playWhenReady = true
                prepare()
                play()

                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_ENDED) {
                            mainHandler.post {
                                // Close VR and return to Flutter
                                (context as? android.app.Activity)?.finish()
                            }
                        }
                    }
                })
            }
        }

        val vertexShader = """
            uniform mat4 uMVP;
            attribute vec3 aPos;
            attribute vec2 aTex;
            varying vec2 vTex;
            void main() {
                vTex = aTex;
                gl_Position = uMVP * vec4(aPos, 1.0);
            }
        """

        val fragmentShader = """
            #extension GL_OES_EGL_image_external : require
            precision mediump float;
            uniform samplerExternalOES uTex;
            varying vec2 vTex;
            void main() {
                gl_FragColor = texture2D(uTex, vTex);
            }
        """

        program = ShaderUtils.createProgram(vertexShader, fragmentShader)

        sensorManager.registerListener(
            this,
            rotationSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        Matrix.perspectiveM(
            projection,
            0,
            90f,
            width.toFloat() / height,
            1f,
            200f
        )
    }

    override fun onDrawFrame(gl: GL10?) {
        surfaceTexture.updateTexImage()

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        Matrix.setLookAtM(
            view,
            0,
            0f,
            0f,
            0f,
            0f,
            0f,
            -1f,
            0f,
            1f,
            0f
        )

        // Apply head rotation
        Matrix.multiplyMM(view, 0, rotationMatrix, 0, view, 0)
        Matrix.multiplyMM(mvp, 0, projection, 0, view, 0)

        GLES20.glUseProgram(program)

        val pos = GLES20.glGetAttribLocation(program, "aPos")
        val tex = GLES20.glGetAttribLocation(program, "aTex")
        val mvpHandle = GLES20.glGetUniformLocation(program, "uMVP")

        GLES20.glEnableVertexAttribArray(pos)
        GLES20.glEnableVertexAttribArray(tex)

        GLES20.glVertexAttribPointer(
            pos,
            3,
            GLES20.GL_FLOAT,
            false,
            0,
            sphere.vertexBuffer
        )
        GLES20.glVertexAttribPointer(
            tex,
            2,
            GLES20.GL_FLOAT,
            false,
            0,
            sphere.texBuffer
        )

        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvp, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, sphere.vertexCount)

        GLES20.glDisableVertexAttribArray(pos)
        GLES20.glDisableVertexAttribArray(tex)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    private fun createOESTexture(): Int {
        val tex = IntArray(1)
        GLES20.glGenTextures(1, tex, 0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0])
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )
        return tex[0]
    }

}