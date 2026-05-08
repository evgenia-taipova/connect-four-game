package components

import androidx.compose.runtime.Composable
import game.GameState
import game.GameStatus
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun Board(state: GameState, onColumnClick: (Int) -> Unit) {
    val winningCells = (state.status as? GameStatus.Win)?.winningCells?.toSet() ?: emptySet()
    val cols = state.config.cols
    // Total horizontal space taken by padding and gaps, leaving the rest for cells
    val reservedPx = 32 + 24 + 6 * (cols - 1) // page padding + board padding + gaps
    val cellSize = "min(60px, calc((100vw - ${reservedPx}px) / $cols))"

    Div(attrs = {
        style {
            display(DisplayStyle.Grid)
            property("grid-template-columns", "repeat($cols, $cellSize)")
            property("gap", "6px")
            backgroundColor(Color("#1565C0"))
            padding(12.px)
            borderRadius(8.px)
        }
    }) {
        for (row in 0 until state.config.rows) {
            for (col in 0 until cols) {
                Cell(
                    cellState = state.board[row][col],
                    isWinning = (row to col) in winningCells,
                    cellSize = cellSize,
                    onClick = { onColumnClick(col) }
                )
            }
        }
    }
}
