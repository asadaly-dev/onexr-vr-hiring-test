package com.example.onexr_vr_app

import android.content.Intent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    // Channels aur constants define kiye
    private val CHANNEL = "com.onexr.vr/player"
    private val VR_REQUEST_CODE = 101
    private var pendingResult: MethodChannel.Result? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Ye code function ke ANDAR hona chahiye
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                if (call.method == "startVrExperience") {
                    // Result ko save kar liya
                    pendingResult = result

                    // Activity start ki
                    val intent = Intent(this, VrPlayerActivity::class.java)
                    startActivityForResult(intent, VR_REQUEST_CODE)
                } else {
                    result.notImplemented()
                }
            }
    }

    // Ye function Class ke andar, lekin configureFlutterEngine ke BAHAR hona chahiye
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VR_REQUEST_CODE) {
            // Jab user wapis aaye, Flutter ko success bhejen
            pendingResult?.success(null)
            pendingResult = null
        }
    }
}