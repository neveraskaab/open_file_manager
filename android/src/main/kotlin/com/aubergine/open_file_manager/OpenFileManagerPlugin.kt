package com.aubergine.open_file_manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import java.io.File
import android.app.DownloadManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** OpenFileManagerPlugin */
class OpenFileManagerPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var context: Context;

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "open_file_manager")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext;
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "openFileManager") {
            openFileManager(result)
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun openFileFlowFolder(result: Result) {
    val context = context.applicationContext
    try {
        val fileFlowDir = context.getExternalFilesDir("FileFlow") // Modify path if needed
        if (fileFlowDir != null && fileFlowDir.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(fileFlowDir), "*/*")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            result.success(true)
        } else {
            result.error("FileFlow folder not found", "", "")
        }
    } catch (e: Exception) {
        result.error("$e", "Unable to open the folder", "")
    }
}
}
