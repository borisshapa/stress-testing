package components

import kotlinx.browser.document
import kotlinx.css.*
import kotlinx.html.dom.create
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.option
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import org.w3c.xhr.XMLHttpRequest
import react.*
import react.dom.form
import styled.css
import styled.styledDiv
import styled.styledSelect

interface CodeSelectProps: RProps {
    var id: Int
    var onChange: (Int) -> Unit
}

data class Code(val id: Int, val name: String, val code: String)

class CodeSelect : RComponent<CodeSelectProps, RState>() {
    override fun componentDidMount() {
        val select = document.getElementById("code_select") as HTMLSelectElement
        select.addEventListener("newOption", {
            props.onChange(getValue())
        })

        val http = XMLHttpRequest()
        http.open("GET", "/code")
        http.onload = {
            val response = JSON.parse<Array<Code>>(http.responseText)
            for (code in response) {
                select.add(document.create.option {
                    + code.name
                    value = code.id.toString()
                } as HTMLOptionElement)
            }
        }
        http.send()
    }

    private fun getValue(): Int {
        val select = document.getElementById("code_select") as HTMLSelectElement
        return select.value.toInt()
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                paddingLeft = 10.px
                width = 150.px
                display = Display.flex
                alignItems = Align.center
            }
            form {
                styledSelect {
                    css {
                        width = 150.px
                        height = 25.px
                        border = "0px solid #E6E6E6"
                        fontFamily = "Montserrat, sans-serif"
                    }
                    attrs {
                        id = "code_select"
                        onChangeFunction = { props.onChange(getValue()) }
                    }
                }
            }
        }
    }
}

fun RBuilder.codeSelect(handler: CodeSelectProps.() -> Unit): ReactElement {
    return child(CodeSelect::class) {
        this.attrs(handler)
    }
}