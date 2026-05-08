import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import components.GameScreen
import game.GameConfig
import game.GameEngine
import game.GameState
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        App()
    }
}

@Composable
fun App() {
    var gameState by remember { mutableStateOf(GameState.initial(GameConfig())) }

    GameScreen(
        state = gameState,
        onColumnClick = { col -> gameState = GameEngine.dropPiece(gameState, col) },
        onNewGame = { gameState = GameState.initial(gameState.config) }
    )
}
