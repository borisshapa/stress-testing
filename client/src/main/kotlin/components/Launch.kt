package components

import kotlinx.browser.document
import kotlinx.css.*
import kotlinx.html.dom.create
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.option
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.xhr.XMLHttpRequest
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.*

interface LaunchState : RState {
    var codes: Array<String>
    var scriptId: Int
}

external fun encodeURIComponent(value: String): String

@Serializable
class LaunchJSON(val codes: Array<String>, val scriptId: Int)

class Launch : RComponent<RProps, LaunchState>() {
    init {
        state.codes = Array(2) { "" }
    }

    private fun runTesting(codes: Array<String>, scriptId: Int) {
        val output = document.getElementById("output") as HTMLTextAreaElement
        output.value = "TESTING..."

        val http = XMLHttpRequest()
        http.open("POST", "/run")
        http.onload = {
            output.value = http.responseText
            Unit
        }
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
        http.send(
            "codes=${encodeURIComponent(JSON.stringify(codes))}&scriptId=${encodeURIComponent(scriptId.toString())}"
        )
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                display = Display.flex
                height = 600.px
            }
            codeInput {
                programName = "First program"
                onChange = { code ->
                    setState(state) {
                        state.codes[0] = code
                    }
                }
            }
            codeInput {
                programName = "Second program"
                onChange = { code ->
                    setState(state) {
                        state.codes[1] = code
                    }
                }
            }
        }
        styledDiv {
            css {
                height = 300.px
            }
            styledDiv {
                css {
                    borderTop = "2px solid black"
                    borderBottom = "2px solid black"
                    height = 40.px
                    fontFamily = "Montserrat, sans-serif"
                    fontWeight = FontWeight.w600
                    display = Display.flex
                }
                styledH3 {
                    css {
                        margin = "0"
                        alignSelf = Align.center
                        marginLeft = 10.px
                    }
                    +"Choose code generating tests"
                }
                codeSelect {
                    onChange = { id ->
                        setState(state) {
                            state.scriptId = id
                        }
                    }
                }

                styledH3 {
                    css {
                        margin = "0"
                        alignSelf = Align.center
                        marginLeft = 10.px
                    }
                    +"or create a new one"
                }

                styledDiv {
                    css {
                        paddingLeft = 10.px
                        width = 30.px
                        display = Display.flex
                        alignItems = Align.center
                    }
                    newScriptModal {}
                }

                styledDiv {
                    css {
                        marginLeft = LinearDimension.auto
                        width = 100.px
                        paddingRight = 15.px
                        display = Display.flex
                        alignItems = Align.center
                    }
                    styledButton {
                        css {
                            backgroundColor = Color("#69E564")
                            fontFamily = "Montserrat, sans-serif"
                            borderRadius = 8.px;
                            width = 100.px
                            height = 35.px
                        }
                        +"Run ►"
                        attrs {
                            onClickFunction = {
                                runTesting(state.codes, state.scriptId)
                            }
                        }
                    }
                }
            }
            styledTextArea {
                css {
                    height = 100.pct
                    width = 100.pct
                    backgroundColor = Color("#E6E6E6")
                    fontFamily = "JetBrains Mono, monospace"
                }
                attrs {
                    id = "output"
                    readonly = true
                }
            }
        }
    }
}

fun RBuilder.launch() = child(Launch::class) {}