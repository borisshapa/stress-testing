package components

import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledH1
import styled.styledHeader

class Header : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledHeader {
            css {
                backgroundColor = Color.black
                height = 4.em
                paddingBottom = 0.em
                color = Color.white
                display = Display.flex
                fontSize = 20.px
            }
            styledH1 {
                css {
                    margin = "0"
                    marginLeft = 0.1.em
                    fontFamily = "Montserrat, sans-serif"
                    fontWeight = FontWeight.w700
                    alignSelf = Align.flexEnd
                }
                +"Stress Testing"
            }
        }
    }
}

fun RBuilder.header() = child(Header::class) {}