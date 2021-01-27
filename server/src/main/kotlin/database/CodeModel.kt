package database

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Codes: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val code = text("code")

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Code(val id: Int, val name: String, val code: String)