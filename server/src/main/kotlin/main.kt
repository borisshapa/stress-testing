import com.beust.klaxon.Klaxon
import database.dao.CodeDatabase
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.css.CSSBuilder
import kotlinx.css.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.Database
import io.ktor.features.ContentNegotiation
import io.ktor.http.*
import io.ktor.jackson.jackson

private val globalCss = CSSBuilder().apply {
    body {
        margin(0.px)
        padding(0.px)
    }
}


fun Application.main() {
    install(ContentNegotiation){
        jackson {}
    }

    val storage = CodeDatabase(Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver"))

    routing {
        get("/") {
            call.respondHtml {
                head {
                    meta {
                        charset = "utf-8"
                    }
                    title {
                        + "Stress testing"
                    }
                    style {
                        unsafe {
                            +globalCss.toString()
                        }
                    }
                    link {
                        rel = "preconnect"
                        href = "https://fonts.gstatic.com"
                    }
                    link {
                        href = "https://fonts.googleapis.com/css2?family=Montserrat:wght@600;700&display=swap"
                        rel = "stylesheet"
                    }
                    link {
                        href="https://fonts.gstatic.com"
                        rel = "preconnect"
                    }
                    link {
                        href="https://fonts.googleapis.com/css2?family=JetBrains+Mono&display=swap"
                        rel = "stylesheet"
                    }
                }
                body {
                    div {
                        id = "root"
                        + "Loading..."
                    }
                    script(src = "/client.js") {
                    }
                }
            }
        }

        route("/code") {
            post {
                val content = call.receiveParameters()
                val name = content["name"]!!
                val code = content["code"]!!
                val id = storage.createCode(name, code)
                call.respond(storage.getCode(id)!!)
            }

            get {
                call.respond(storage.getAllCodes())
            }
        }

        post("/run") {
            val parameters = call.receiveParameters()
            println(parameters)
            val codes = Klaxon().parseArray<String>(parameters["codes"]!!)!!
            val scriptId = parameters["scriptId"]!!

            val scriptCode = storage.getCode(scriptId.toInt())!!.code

            println(codes)
            println(scriptCode)
            val response = runTesting(codes[0], codes[1], scriptCode)

            println(response)
            call.respond(response)
        }

        static {
            resources("/")
        }
    }
}
