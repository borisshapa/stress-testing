package components

import components.jsImports.editor
import kotlinx.css.*
import react.*
import styled.css
import styled.styledDiv
import styled.styledH3

external interface CodeInputProps : RProps {
    var programName: String
    var onChange: (String) -> Unit
}

@JsModule("ace-builds/src-noconflict/mode-c_cpp")
external object CCppMode

@JsModule("ace-builds/src-noconflict/theme-github")
external object GithubTheme

class Loader(private val idInternal: String, private val loader: () -> Unit) {
    val id: String get() { loader(); return idInternal }
}

class CodeInput : RComponent<CodeInputProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                flexGrow = 1.0
                margin = "10px"
                display = Display.flex
                flexDirection = FlexDirection.column
            }
            styledH3 {
                css {
                    fontFamily = "Montserrat, sans-serif"
                    fontWeight = FontWeight.w600
                    marginBottom = 1.px
                    marginTop = 0.5.em
                }
                +props.programName
            }
            editor {
                attrs {
                    mode = Loader("c_cpp") { CCppMode }.id
                    theme = Loader("github") { GithubTheme }.id
                    onChange = { code -> props.onChange(code) }
                    fontSize = 18
                    width = "100%"
                    height = "100%"
                }
            }
        }
    }
}


fun RBuilder.codeInput(handler: CodeInputProps.() -> Unit): ReactElement {
    return child(CodeInput::class) {
        this.attrs(handler)
    }
}