package components

import androidx.compose.runtime.Composable
import game.CellState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun Cell(cellState: CellState, isWinning: Boolean = false, cellSize: String = "60px", onClick: () -> Unit) {
    Div(attrs = {
        style {
            property("width", cellSize)
            property("height", cellSize)
            borderRadius(50.percent)
            backgroundColor(
                when {
                    isWinning && cellState == CellState.RED -> Color("#FF1744")
                    isWinning && cellState == CellState.YELLOW -> Color("#FFD600")
                    cellState == CellState.RED -> Color("#F44336")
                    cellState == CellState.YELLOW -> Color("#FDD835")
                    else -> Color("#BBDEFB")
                }
            )
            if (isWinning) {
                property("box-shadow", "0 0 0 3px white inset")
            }
            property("cursor", "pointer")
            property("transition", "background-color 0.15s")
        }
        onClick { onClick() }
    })
}
