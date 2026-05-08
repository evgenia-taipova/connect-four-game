package game

object GameEngine {

    fun dropPiece(state: GameState, col: Int): GameState {
        if (state.status !is GameStatus.Playing) return state
        if (col < 0 || col >= state.config.cols) return state

        val targetRow = (state.config.rows - 1 downTo 0)
            .firstOrNull { state.board[it][col] == CellState.EMPTY }
            ?: return state

        val piece = if (state.currentPlayer == Player.RED) CellState.RED else CellState.YELLOW

        val newBoard = state.board.mapIndexed { r, rowCells ->
            if (r == targetRow) rowCells.mapIndexed { c, cell -> if (c == col) piece else cell }
            else rowCells
        }

        val newStatus: GameStatus = findWin(newBoard, state.config)
            ?: if (isDraw(newBoard)) GameStatus.Draw else GameStatus.Playing

        val nextPlayer = if (state.currentPlayer == Player.RED) Player.YELLOW else Player.RED

        return state.copy(
            board = newBoard,
            currentPlayer = if (newStatus is GameStatus.Playing) nextPlayer else state.currentPlayer,
            status = newStatus
        )
    }

    private fun findWin(board: List<List<CellState>>, config: GameConfig): GameStatus.Win? {
        val rows = config.rows
        val cols = config.cols
        val n = config.winCondition
        val directions = listOf(
            0 to 1,
            1 to 0,
            1 to 1,
            1 to -1
        )

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val cell = board[r][c]
                if (cell == CellState.EMPTY) continue
                for ((dr, dc) in directions) {
                    val cells = (0 until n).map { i -> r + i * dr to c + i * dc }
                    if (cells.all { (nr, nc) ->
                            nr in 0 until rows && nc in 0 until cols && board[nr][nc] == cell
                        }) {
                        val player = if (cell == CellState.RED) Player.RED else Player.YELLOW
                        return GameStatus.Win(player, cells)
                    }
                }
            }
        }
        return null
    }

    fun statusFromBoard(board: List<List<CellState>>, config: GameConfig): GameStatus =
        findWin(board, config) ?: if (isDraw(board)) GameStatus.Draw else GameStatus.Playing

    private fun isDraw(board: List<List<CellState>>): Boolean =
        board.all { row -> row.all { it != CellState.EMPTY } }
}
