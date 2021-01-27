package components

import components.jsImports.ReactModal
import components.jsImports.editor
import kotlinext.js.jsObject
import kotlinx.browser.document
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.dom.create
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.option
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.xhr.XMLHttpRequest
import react.*
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledInput

interface NewScriptModalProps : RProps {
    var isOpen: Boolean
    var onAfterOpen: () -> Unit
    var onRequestClose: () -> Unit
    var onDelete: () -> Unit
}

interface NewScriptModalState : RState {
    var script: String
    var name: String
    var showModal: Boolean
}

class NewScriptModal : RComponent<NewScriptModalProps, NewScriptModalState>() {
    private fun openModal() {
        setState {
            showModal = true
        }
    }

    private fun getName(): String {
        val nameInput = document.getElementById("name_input") as HTMLInputElement
        return nameInput.value
    }

    private fun closeModal() {
        setState {
            showModal = false
            name = getName()
        }
    }

    private fun saveScript() {
        console.log("Save script...")
        val http = XMLHttpRequest()
        http.open("POST", "/code")
        http.onload = {
            val select = document.getElementById("code_select") as HTMLSelectElement
            if (http.status in 200..399) {
                val response = JSON.parse<Code>(http.responseText)
                select.add(document.create.option {
                    + response.name
                    value = response.id.toString()
                    selected = true
                } as HTMLOptionElement)
                select.dispatchEvent(Event("newOption"))
                closeModal()
            }
        }
        http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")
        http.send("code=${encodeURIComponent(state.script)}&name=${encodeURIComponent(getName())}")
    }

    override fun RBuilder.render() {
        styledButton {
            css {
                backgroundColor = Color("#E6E6E6")
                fontFamily = "Montserrat, sans-serif"
                borderRadius = 8.px;
                width = 30.px
                height = 30.px
            }
            +"➕"
            attrs {
                onClickFunction = { openModal() }
            }
        }

        ReactModal {
            attrs {
                isOpen = state.showModal
                onAfterOpen = {
                    val name = state.name
                    if (name != undefined) {
                        (document.getElementById("name_input") as HTMLInputElement).value = name
                    }
                }
                onRequestClose = { closeModal() }
                style = jsObject {
                    overlay = jsObject {
                        zIndex = "1000"
                    }
                    content = jsObject {
                        position = "absolute"
                        left = "6rem"
                        right = "6rem"
                        top = "6rem"
                        bottom = "6rem"
                        borderRadius = "4px"
                        border = "2px solid #000000"
                        overflow = "hidden"
                    }
                }
                contentLabel = "New testing script"
            }
            styledDiv {
                css {
                    width = 100.pct
                    height = 100.pct
                    display = Display.flex
                    alignContent = Align.stretch
                    flexDirection = FlexDirection.column
                }
                editor {
                    attrs {
                        mode = Loader("c_cpp") { CCppMode }.id
                        theme = Loader("github") { GithubTheme }.id
                        value = state.script
                        onChange = { code ->
                            setState {
                                script = code
                            }
                        }
                        fontSize = 18
                        width = "100%"
                        height = "100%"
                    }
                }.props
                styledDiv {
                    css {
                        display = Display.flex
                        paddingTop = 15.px
                    }
                    styledInput {
                        css {
                            fontSize = 16.px
                            fontFamily = "Montserrat, sans-serif"
                            width = 100.pct
                            border = "0px solid #E6E6E6"
                            backgroundColor = Color("#E6E6E6")
                        }
                        attrs {
                            id = "name_input"
                            type = InputType.text
                            placeholder = "Name"
                        }
                    }
                    styledDiv {
                        css {
                            paddingLeft = 20.px
                        }
                        styledButton {
                            css {
                                backgroundColor = Color("#69E564")
                                fontFamily = "Montserrat, sans-serif"
                                borderRadius = 8.px;
                                width = 70.px
                                height = 35.px
                            }
                            +"Save"
                            attrs {
                                onClickFunction = { saveScript() }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.newScriptModal(handler: NewScriptModalProps.() -> Unit): ReactElement {
    return child(NewScriptModal::class) {
        this.attrs(handler)
    }
}