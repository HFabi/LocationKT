package com.example.lenovo.mylocationkt.controllers

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.example.lenovo.mylocationkt.activities.base.BaseActivity
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val PERMISSION_REQUEST = 0

interface PermissionController {
    suspend fun requestPermission(permission: String): Boolean
}

class PermissionControllerImpl(private val baseActivity: BaseActivity) : PermissionController {

    private var permissionListener: PermissionListener? = null

    override suspend fun requestPermission(permission: String): Boolean {
        return suspendCoroutine { continuation ->

            val permissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    Timber.d("Request Permission Callback: granted")
                    continuation.resume(true)
                    permissionListener = null
                }

                override fun onPermissionDenied() {
                    Timber.d("Request Permission Callback: denied")
                    continuation.resume(false)
                    permissionListener = null
                }
            }

            baseActivity.setPermissionListener(permissionListener)
            if (ContextCompat.checkSelfPermission(baseActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(baseActivity, permission)) {
                    Timber.d("Request Permission IF ")
                    permissionListener?.onPermissionDenied()
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    Timber.d("Request Permission")
                    ActivityCompat.requestPermissions(baseActivity, arrayOf(permission), PERMISSION_REQUEST)
                }
            } else {
                permissionListener?.onPermissionGranted()
            }
        }
    }
}

interface PermissionListener {
    fun onPermissionGranted()
    fun onPermissionDenied()
}