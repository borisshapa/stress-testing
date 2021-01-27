package database.dao

import database.Code
import database.Codes
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class CodeDatabase(val db: Database) : CodeStorage {
    init {
        transaction(db) {
            SchemaUtils.create(Codes)
        }
    }

    override fun createCode(name: String, code: String): Int = transaction(db) {
        Codes.insert {
            it[Codes.name] = name;
            it[Codes.code] = code;
        }[Codes.id]
    }

    override fun updateCode(id: Int, name: String, code: String) = transaction(db) {
        Codes.update({ Codes.id eq id }) {
            it[Codes.name] = name
            it[Codes.code] = code
        }
        Unit
    }

    override fun deleteCode(id: Int) = transaction(db) {
        Codes.deleteWhere { Codes.id eq id }
        Unit
    }

    override fun getCode(id: Int): Code? = transaction(db) {
        Codes.select { Codes.id eq id }.map {
            Code(it[Codes.id], it[Codes.name], it[Codes.code])
        }.singleOrNull()
    }

    override fun getAllCodes(): List<Code> = transaction(db) {
        Codes.selectAll().map {
            Code(it[Codes.id], it[Codes.name], it[Codes.code])
        }
    }

    override fun close() {
    }
}