package components

import AppStyle
import androidx.compose.runtime.Composable
import game.CellState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun Cell(cellState: CellState, isWinning: Boolean = false, isLastDrop: Boolean = false, row: Int = 0, cellSize: String = "60px") {
    Div(attrs = {
        style {
            property("width", cellSize)
            property("height", cellSize)
            borderRadius(50.percent)
            backgroundColor(Color("#0D47A1"))
            position(Position.Relative)
        }
    }) {
        if (cellState != CellState.EMPTY) {
            Div(attrs = {
                if (isLastDrop) classes(AppStyle.droppingPiece)
                style {
                    if (isLastDrop) {
                        property("--fall-rows", (row + 1).toString())
                    }
                    position(Position.Absolute)
                    top(0.px)
                    left(0.px)
                    property("width", "100%")
                    property("height", "100%")
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
                }
            })
        }
    }
}
