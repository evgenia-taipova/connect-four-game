import game.*
import kotlinx.browser.localStorage

private const val SAVE_KEY = "connect-four-save"

fun saveGameState(state: GameState) {
    val boardStr = state.board.flatten().joinToString(",") {
        when (it) {
            CellState.EMPTY -> "0"
            CellState.RED -> "1"
            CellState.YELLOW -> "2"
        }
    }
    val data = "${state.config.rows}|${state.config.cols}|${state.config.winCondition}" +
            "|${state.currentPlayer.ordinal}|$boardStr"
    localStorage.setItem(SAVE_KEY, data)
}

fun loadGameState(): GameState? {
    return try {
        val data = localStorage.getItem(SAVE_KEY) ?: return null
        val sections = data.split("|")
        if (sections.size != 5) return null

        val rows = sections[0].toInt()
        val cols = sections[1].toInt()
        val winCondition = sections[2].toInt()
        val playerOrdinal = sections[3].toInt()
        val cellValues = sections[4].split(",")

        if (cellValues.size != rows * cols) return null

        val board = cellValues.map {
            when (it) {
                "1" -> CellState.RED
                "2" -> CellState.YELLOW
                else -> CellState.EMPTY
            }
        }.chunked(cols)

        val config = GameConfig(rows, cols, winCondition)
        val currentPlayer = Player.entries.getOrNull(playerOrdinal) ?: return null
        val status = GameEngine.statusFromBoard(board, config)

        GameState(board, currentPlayer, status, config)
    } catch (e: Exception) {
        null
    }
}

fun clearGameState() {
    localStorage.removeItem(SAVE_KEY)
}
