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

    private fun openFileManager(result: Result) {
    try {
        // Get the external storage directory and append your specific path
        val fileManagerIntent = Intent(Intent.ACTION_VIEW)
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val specificFolderPath = File(downloadsPath, "FileFlow")

        // Check if the folder exists, if not, create it
        if (!specificFolderPath.exists()) {
            specificFolderPath.mkdirs() // Create the folder if it doesn't exist
        }

        // Set the data and type on the intent to open the directory
        fileManagerIntent.setDataAndType(Uri.parse(specificFolderPath.toURI().toString()), "resource/folder")
        fileManagerIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Start the activity with the intent
        context.startActivity(fileManagerIntent)
        result.success(true)
    } catch (e: Exception) {
        result.error("ERROR", "Unable to open the file manager: ${e.localizedMessage}", "")
    }
}
}
