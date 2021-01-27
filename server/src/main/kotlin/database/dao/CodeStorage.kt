package database.dao

import database.Code
import java.io.Closeable

interface CodeStorage: Closeable {
    fun createCode(name: String, code: String): Int
    fun updateCode(id: Int, name: String, code: String)
    fun deleteCode(id: Int)
    fun getCode(id: Int): Code?
    fun getAllCodes(): List<Code>
}