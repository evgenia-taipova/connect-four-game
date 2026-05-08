package game

data class GameState(
    val board: List<List<CellState>>,
    val currentPlayer: Player,
    val status: GameStatus,
    val config: GameConfig
) {
    companion object {
        fun initial(config: GameConfig): GameState = GameState(
            board = List(config.rows) { List(config.cols) { CellState.EMPTY } },
            currentPlayer = Player.RED,
            status = GameStatus.Playing,
            config = config
        )
    }
}
