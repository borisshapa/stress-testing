package components.jsImports

import kotlinext.js.TemplateTag
import kotlinx.css.CSSBuilder
import org.w3c.dom.css.CSS
import react.RClass
import react.RProps
import styled.CssHolder

@JsModule("react-modal")
@JsNonModule
external val ReactModal : ReactModalClass

external interface ReactModalProps : RProps {
    var isOpen: Boolean
    var onAfterOpen: () -> Unit
    var onAfterClose: () -> Unit
    var onRequestClose: () -> Unit
    var closeTimeoutMS: Int
    var contentLabel: String
    var portalClassName: String
    var overlayClassName: ModalTransitionClasses
    var id: String
    var className: ModalTransitionClasses
    var bodyOpenClassName: String
    var htmlOpenClassName: String
    var ariaHideApp: Boolean
    var shouldFocusAfterRender: Boolean
    var shouldCloseOnOverlayClick: Boolean
    var shouldCloseOnEsc: Boolean
    var style: StyleClass
    var role: String
}

external interface StyleClass {
    var content: dynamic
    var overlay: dynamic
}

external interface ModalTransitionClasses {
    var base: String
    var afterOpen: String
    var beforeClose: String
}

external interface ReactModalClass : RClass<ReactModalProps> {
    fun setAppElement(selector: String)
}