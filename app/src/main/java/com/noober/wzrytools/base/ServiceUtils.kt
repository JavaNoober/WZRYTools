package com.noober.wzrytools.base

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter

object ServiceUtils {
    fun openService(context: Context) {
        if (!isAccessibilityOpened(context)) {
            context.startService(Intent(context, AccessibilityService::class.java))
            //打开系统设置中辅助功能
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private fun isAccessibilityOpened(context: Context): Boolean {
        val serviceName =
            context.packageName + "/" + AccessibilityService::class.java.getCanonicalName()
        return serviceOpen(serviceName, context.applicationContext)
//        return AppUtil.isAccessibilityServiceRunning(WeChatAssistAccessService.getInstance());
    }

    private fun serviceOpen(service: String?, context: Context): Boolean {
        var ok = 0
        try {
            ok = Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }
        val ms = SimpleStringSplitter(':')
        if (ok == 1) {
            val settingValue = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                ms.setString(settingValue)
                while (ms.hasNext()) {
                    val accessibilityService = ms.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}