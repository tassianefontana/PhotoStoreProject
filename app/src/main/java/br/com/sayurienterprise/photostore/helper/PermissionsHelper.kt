package br.com.sayurienterprise.photostore.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PermissionsHelper(private val context: Context) {
    private var permissions = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val _permissionsGranted = MutableLiveData<Boolean>()
    val permissionsGranted: LiveData<Boolean> get() = _permissionsGranted

    fun requestPermissionsIfNecessary(activity: android.app.Activity) {
        if (!hasAllPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                activity,
                getPermissions(),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            _permissionsGranted.value = true
        }
    }

    fun onRequestPermissionsResult(requestCode: Int) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            _permissionsGranted.value = hasAllPermissionsGranted()
        }
    }

    private fun hasAllPermissionsGranted(): Boolean {
        return getPermissions().isEmpty()   }

    private fun getPermissions(): Array<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 1001
    }
}

