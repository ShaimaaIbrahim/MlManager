package com.example.newproject.utils

import android.os.Build
import com.example.newproject.MlManagerAppliciation
import java.io.File

class UtilsRoot {

companion object{
    private val ROOT_STATUS_NOT_CHECKED = 0
    private val ROOT_STATUS_ROOTED = 1
    private val ROOT_STATUS_NOT_ROOTED = 2


    fun isRooted(): Boolean {

        val rootStatus: Int =MlManagerAppliciation.appPreferences.getRootStatus()
        var isRooted = false
        if (rootStatus == ROOT_STATUS_NOT_CHECKED) {
            isRooted = isRootByBuildTag() || isRootedByFileSU() || isRootedByExecutingCommand()
            MlManagerAppliciation.appPreferences.setRootStatus(if (isRooted) ROOT_STATUS_ROOTED else ROOT_STATUS_NOT_ROOTED)
        } else if (rootStatus == ROOT_STATUS_ROOTED) {
            isRooted = true
        }
        return isRooted
    }

    fun isRootByBuildTag(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    fun isRootedByFileSU(): Boolean {
        try {
            val file = File("/system/app/Superuser.apk")
            if (file.exists()) {
                return true
            }
        } catch (e1: Exception) {
        }
        return false
    }

    fun isRootedByExecutingCommand(): Boolean {
        return (canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su")
                || canExecuteCommand("which su"))
    }

    fun removeWithRootPermission(directory: String): Boolean {
        var status = false
        try {
            val command = arrayOf("su", "-c", "rm -rf $directory")
            val process = Runtime.getRuntime().exec(command)
            process.waitFor()
            val i = process.exitValue()
            if (i == 0) {
                status = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return status
    }

    fun hideWithRootPermission(apk: String, hidden: Boolean): Boolean {
        var status = false
        try {
            val command: Array<String>
            command = if (hidden) {
                arrayOf("su", "-c", "pm unhide $apk\n")
            } else {
                arrayOf("su", "-c", "pm hide $apk\n")
            }
            val process = Runtime.getRuntime().exec(command)
            process.waitFor()
            val i = process.exitValue()
            if (i == 0) {
                status = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return status
    }

    fun uninstallWithRootPermission(source: String): Boolean {
        var status = false
        try {
            val command_write = arrayOf("su", "-c", "mount -o rw,remount /system\n")
            val command_delete = arrayOf("su", "-c", "rm -r /$source\n")
            val command_read = arrayOf("su", "-c", "mount -o ro,remount /system\n")
            var process = Runtime.getRuntime().exec(command_write)
            process.waitFor()
            var i = process.exitValue()
            if (i == 0) {
                process = Runtime.getRuntime().exec(command_delete)
                process.waitFor()
                i = process.exitValue()
                if (i == 0) {
                    process = Runtime.getRuntime().exec(command_read)
                    process.waitFor()
                    i = process.exitValue()
                    if (i == 0) {
                        status = true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return status
    }

    fun rebootSystem(): Boolean {
        var status = false
        try {
            val command = arrayOf("su", "-c", "reboot\n")
            val process = Runtime.getRuntime().exec(command)
            process.waitFor()
            val i = process.exitValue()
            if (i == 0) {
                status = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return status
    }

    fun getFolderSizeInMB(directory: String?): Long {
        val f = File(directory)
        var size: Long = 0
        if (f.isDirectory) {
            for (file in f.listFiles()) {
                size += getFolderSizeInMB(file.absolutePath)
            }
        } else {
            size = f.length() / 1024 / 2024
        }
        return size
    }

    private fun canExecuteCommand(command: String): Boolean {
        val isExecuted: Boolean
        isExecuted = try {
            Runtime.getRuntime().exec(command)
            true
        } catch (e: Exception) {
            false
        }
        return isExecuted
    }
}

}