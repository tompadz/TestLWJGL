package engine.uilts

import java.io.File
import java.io.IOException
import java.lang.StringBuilder

class FileUtil {

    fun loadAsString(path:String) : String {
        val result = StringBuilder()
        try {
           val file = File(path)
            file.readLines().forEach {
                result.append(it).append("\n")
            }
        }catch (e: IOException) {
            System.err.println("file read error - ${e.message.toString()}")
        }

        return result.toString()
    }

}