package utils

import java.util.*

class SystemUtil {

    fun getOS(): OS {
        val os = System.getProperty("os.name", "generic").lowercase(Locale.ENGLISH)
        return when {
            os.contains("mac") || os.contains("darwin") -> OS.MAC
            os.contains("win") -> OS.WIN
            os.contains("nux") -> OS.LINUX
            else -> OS.OTHER
        }
    }

    enum class OS {
        MAC,
        WIN,
        LINUX,
        OTHER,
    }
}