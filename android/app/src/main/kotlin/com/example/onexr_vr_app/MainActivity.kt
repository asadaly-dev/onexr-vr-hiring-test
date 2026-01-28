package com.example.onexr_vr_app

import android.content.Intent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {


    private val CHANNEL = "com.onexr.vr/player"
    private val VR_REQUEST_CODE = 101
    private var pendingResult: MethodChannel.Result? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)


        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                if (call.method == "startVrExperience") {

                    pendingResult = result


                    val intent = Intent(this, VrPlayerActivity::class.java)
                    startActivityForResult(intent, VR_REQUEST_CODE)
                } else {
                    result.notImplemented()
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VR_REQUEST_CODE) {
            pendingResult?.success(null)
            pendingResult = null
        }
    }
}