package components

import androidx.compose.runtime.Composable
import game.GameState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun Board(state: GameState, onColumnClick: (Int) -> Unit) {
    Div(attrs = {
        style {
            display(DisplayStyle.Grid)
            property("grid-template-columns", "repeat(${state.config.cols}, 60px)")
            property("gap", "6px")
            backgroundColor(Color("#1565C0"))
            padding(12.px)
            borderRadius(8.px)
        }
    }) {
        for (row in 0 until state.config.rows) {
            for (col in 0 until state.config.cols) {
                Cell(
                    cellState = state.board[row][col],
                    onClick = { onColumnClick(col) }
                )
            }
        }
    }
}
