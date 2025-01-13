package com.example.login.presentation.utils

import android.content.Context
import android.content.UriPermission
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionUtils {
    fun isPermissionsGranted(context: Context, permission: String): Boolean{
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: androidx.fragment.app.FragmentActivity, permission: Array<String>, requestCode:Int){
        ActivityCompat.requestPermissions(activity,permission,requestCode)
    }
}