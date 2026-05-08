import org.jetbrains.compose.web.css.*

object AppStyle : StyleSheet() {
    val drop by keyframes {
        from {
            property("transform", "translateY(calc(var(--fall-rows, 1) * -100%))")
        }
        to {
            property("transform", "translateY(0)")
        }
    }

    val droppingPiece by style {
        animation(drop) {
            duration(0.3.s)
            timingFunction(AnimationTimingFunction.EaseIn)
        }
    }
}
