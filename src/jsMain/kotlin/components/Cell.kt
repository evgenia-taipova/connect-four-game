package components

import androidx.compose.runtime.Composable
import game.CellState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun Cell(cellState: CellState, onClick: () -> Unit) {
    Div(attrs = {
        style {
            width(60.px)
            height(60.px)
            borderRadius(50.percent)
            backgroundColor(
                when (cellState) {
                    CellState.EMPTY -> Color("#BBDEFB")
                    CellState.RED -> Color("#F44336")
                    CellState.YELLOW -> Color("#FDD835")
                }
            )
            property("cursor", "pointer")
            property("transition", "background-color 0.15s")
        }
        onClick { onClick() }
    })
}
