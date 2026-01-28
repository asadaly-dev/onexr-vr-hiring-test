package com.example.onexr_vr_app
import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class VrPlayerActivity : Activity() {

    private lateinit var glView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glView = GLSurfaceView(this)
        glView.setEGLContextClientVersion(2)
        glView.setRenderer(VrSphereRenderer(this))
        glView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

        setContentView(glView)
    }

    override fun onPause() {
        super.onPause()
        glView.onPause()
    }

    override fun onResume() {
        super.onResume()
        glView.onResume()
    }
}
