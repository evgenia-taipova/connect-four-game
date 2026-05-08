package game

sealed class GameStatus {
    object Playing : GameStatus()
    data class Win(val player: Player, val winningCells: List<Pair<Int, Int>>) : GameStatus()
    object Draw : GameStatus()
}
