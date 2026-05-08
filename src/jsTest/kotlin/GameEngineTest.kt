import game.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GameEngineTest {

    private val config = GameConfig(rows = 6, cols = 7, winCondition = 4)

    private fun stateWithBoard(vararg rows: String): GameState {
        val board = rows.map { row ->
            row.map { c ->
                when (c) {
                    'R' -> CellState.RED
                    'Y' -> CellState.YELLOW
                    else -> CellState.EMPTY
                }
            }
        }
        return GameState(
            board = board,
            currentPlayer = Player.RED,
            status = GameStatus.Playing,
            config = GameConfig(rows = board.size, cols = board[0].size, winCondition = 4)
        )
    }

    @Test
    fun piece_fallsToBottom() {
        val state = GameState.initial(config)
        val next = GameEngine.dropPiece(state, 3)
        assertEquals(CellState.RED, next.board[5][3])
    }

    @Test
    fun pieces_stackOnEachOther() {
        var state = GameState.initial(config)
        state = GameEngine.dropPiece(state, 0) // RED at (5,0)
        state = GameEngine.dropPiece(state, 0) // YELLOW at (4,0)
        assertEquals(CellState.RED, state.board[5][0])
        assertEquals(CellState.YELLOW, state.board[4][0])
    }

    @Test
    fun player_switchesAfterMove() {
        val state = GameState.initial(config)
        val next = GameEngine.dropPiece(state, 0)
        assertEquals(Player.YELLOW, next.currentPlayer)
    }

    @Test
    fun dropPiece_ignoresFullColumn() {
        val c = GameConfig(rows = 3, cols = 3, winCondition = 5)
        var state = GameState.initial(c)
        state = GameEngine.dropPiece(state, 0) // RED (2,0)
        state = GameEngine.dropPiece(state, 1) // YELLOW
        state = GameEngine.dropPiece(state, 0) // RED (1,0)
        state = GameEngine.dropPiece(state, 1) // YELLOW
        state = GameEngine.dropPiece(state, 0) // RED (0,0) — col 0 full, game still Playing
        assertEquals(GameStatus.Playing, state.status)
        val full = state
        assertEquals(full, GameEngine.dropPiece(full, 0))
    }

    @Test
    fun dropPiece_ignoresFinishedGame() {
        var state = GameState.initial(config)
        state = GameEngine.dropPiece(state, 0); state = GameEngine.dropPiece(state, 0)
        state = GameEngine.dropPiece(state, 1); state = GameEngine.dropPiece(state, 1)
        state = GameEngine.dropPiece(state, 2); state = GameEngine.dropPiece(state, 2)
        state = GameEngine.dropPiece(state, 3) // RED wins horizontally
        assertIs<GameStatus.Win>(state.status)
        assertEquals(state, GameEngine.dropPiece(state, 4))
    }

    @Test
    fun win_horizontal() {
        var state = GameState.initial(config)
        state = GameEngine.dropPiece(state, 0); state = GameEngine.dropPiece(state, 0)
        state = GameEngine.dropPiece(state, 1); state = GameEngine.dropPiece(state, 1)
        state = GameEngine.dropPiece(state, 2); state = GameEngine.dropPiece(state, 2)
        state = GameEngine.dropPiece(state, 3)
        val status = state.status
        assertIs<GameStatus.Win>(status)
        assertEquals(Player.RED, status.player)
    }

    @Test
    fun win_vertical() {
        var state = GameState.initial(config)
        repeat(3) {
            state = GameEngine.dropPiece(state, 0) // RED
            state = GameEngine.dropPiece(state, 1) // YELLOW
        }
        state = GameEngine.dropPiece(state, 0) // RED — 4th in col 0
        val status = state.status
        assertIs<GameStatus.Win>(status)
        assertEquals(Player.RED, status.player)
    }

    @Test
    fun win_diagonalDownRight() {
        // RED will land at (2,2); existing RED at (3,3),(4,4),(5,5)
        val state = stateWithBoard(
            ".......",
            ".......",
            ".......",
            "..YR...",
            "..Y.R..",
            "..Y..R."
        )
        val next = GameEngine.dropPiece(state, 2)
        val status = next.status
        assertIs<GameStatus.Win>(status)
        assertEquals(Player.RED, status.player)
    }

    @Test
    fun win_diagonalDownLeft() {
        // RED will land at (2,3); existing RED at (3,2),(4,1),(5,0)
        val state = stateWithBoard(
            ".......",
            ".......",
            ".......",
            "..RY...",
            ".R.Y...",
            "R..Y..."
        )
        val next = GameEngine.dropPiece(state, 3)
        val status = next.status
        assertIs<GameStatus.Win>(status)
        assertEquals(Player.RED, status.player)
    }

    @Test
    fun noFalseWin_threeInARow() {
        var state = GameState.initial(config)
        state = GameEngine.dropPiece(state, 0); state = GameEngine.dropPiece(state, 4)
        state = GameEngine.dropPiece(state, 1); state = GameEngine.dropPiece(state, 5)
        state = GameEngine.dropPiece(state, 2) // RED — 3 in a row, not 4
        assertEquals(GameStatus.Playing, state.status)
    }

    @Test
    fun draw_fullBoardNoWinner() {
        val c = GameConfig(rows = 2, cols = 2, winCondition = 3)
        var state = GameState.initial(c)
        state = GameEngine.dropPiece(state, 0) // RED (1,0)
        state = GameEngine.dropPiece(state, 1) // YELLOW (1,1)
        state = GameEngine.dropPiece(state, 0) // RED (0,0)
        state = GameEngine.dropPiece(state, 1) // YELLOW (0,1) — board full, no winner
        assertEquals(GameStatus.Draw, state.status)
    }
}
