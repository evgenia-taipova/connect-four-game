package components

import androidx.compose.runtime.Composable
import game.GameState
import game.GameStatus
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun Board(state: GameState, lastDrop: Pair<Int, Int>? = null, onColumnClick: (Int) -> Unit) {
    val winningCells = (state.status as? GameStatus.Win)?.winningCells?.toSet() ?: emptySet()
    val cols = state.config.cols
    val reservedPx = 32 + 24 + 6 * (cols - 1)
    val cellSize = "min(60px, calc((100vw - ${reservedPx}px) / $cols))"

    Div(attrs = {
        style {
            display(DisplayStyle.Flex)
            property("flex-direction", "row")
            property("gap", "6px")
            backgroundColor(Color("#1565C0"))
            padding(12.px)
            borderRadius(8.px)
        }
    }) {
        for (col in 0 until cols) {
            Div(attrs = {
                style {
                    display(DisplayStyle.Flex)
                    property("flex-direction", "column")
                    property("gap", "6px")
                    property("overflow", "hidden")
                    property("cursor", "pointer")
                }
                onClick { onColumnClick(col) }
            }) {
                for (row in 0 until state.config.rows) {
                    Cell(
                        cellState = state.board[row][col],
                        isWinning = (row to col) in winningCells,
                        isLastDrop = lastDrop == (row to col),
                        row = row,
                        cellSize = cellSize
                    )
                }
            }
        }
    }
}
