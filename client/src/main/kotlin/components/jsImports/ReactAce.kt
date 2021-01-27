@file:JsModule("react-ace")
@file:JsNonModule
package components.jsImports

import react.RClass
import react.RProps

@JsName("default")
external val editor: RClass<AceEditorProps>

external interface AceEditorProps: RProps {
    var mode: String
    var theme: String
    var value: String
    var onChange: (String) -> Unit
    var name: String
    var fontSize: Int
    var width: String
    var height: String
}