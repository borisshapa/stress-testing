import components.codeInput
import kotlinx.browser.document
import react.dom.*
import components.header
import components.jsImports.ReactModal
import components.launch
import kotlinx.css.*
import styled.*

fun main() {
    GlobalStyles.inject()

    ReactModal.setAppElement("#root")

    render(document.getElementById("root")) {
        header()

        launch()
    }
}