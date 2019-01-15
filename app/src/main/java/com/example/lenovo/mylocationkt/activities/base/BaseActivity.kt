package com.example.lenovo.mylocationkt.activities.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.lenovo.mylocationkt.controllers.PERMISSION_REQUEST
import com.example.lenovo.mylocationkt.controllers.PermissionListener
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

  private var permissionListener: PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
    }

    abstract fun getLayoutRes(): Int

    fun setPermissionListener(permissionListener: PermissionListener) {
        this.permissionListener = permissionListener
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d("TAGING","onRequestPermissionResult")
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAGING","Permission granted")
                permissionListener?.onPermissionGranted()
            } else {
                Log.d("TAGING","Permission denied or callback null")
                permissionListener?.onPermissionDenied()
            }
        }
    }
}