import java.io.File
import java.lang.StringBuilder

@Volatile
var id = 1
    get() {
        return field++
    }

fun runTesting(code1: String, code2: String, script: String): String {
    val dirId = id
    val dir = File("tmp/code$dirId")
    dir.mkdirs()

    val code1File = File(dir, "code1.cpp")
    code1File.printWriter().use { out ->
        out.println(code1)
    }

    val code2File = File(dir, "code2.cpp")
    code2File.printWriter().use { out ->
        out.println(code2)
    }

    val scriptFile = File(dir, "script.cpp")
    scriptFile.printWriter().use { out ->
        out.println(script)
    }

    val proc = Runtime.getRuntime().exec("./tmp/test.sh tmp/code${dirId}")
    val stdInput = proc.inputStream.bufferedReader()

    val sb = StringBuilder()
    val iterator = stdInput.lineSequence().iterator()

    while(iterator.hasNext()) {
        sb.append("${iterator.next()}\n")
    }
    stdInput.close()

    dir.deleteRecursively()
    return sb.toString()
}