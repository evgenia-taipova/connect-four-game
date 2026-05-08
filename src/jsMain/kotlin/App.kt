import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import components.ConfigScreen
import components.GameScreen
import game.GameConfig
import game.GameEngine
import game.GameState
import org.jetbrains.compose.web.renderComposable

sealed class AppScreen {
    object Config : AppScreen()
    data class Game(val config: GameConfig) : AppScreen()
}

fun main() {
    renderComposable(rootElementId = "root") {
        App()
    }
}

@Composable
fun App() {
    var screen by remember { mutableStateOf<AppScreen>(AppScreen.Config) }

    when (val s = screen) {
        is AppScreen.Config -> ConfigScreen(
            onStart = { config -> screen = AppScreen.Game(config) }
        )
        is AppScreen.Game -> {
            var gameState by remember(s) { mutableStateOf(GameState.initial(s.config)) }
            GameScreen(
                state = gameState,
                onColumnClick = { col -> gameState = GameEngine.dropPiece(gameState, col) },
                onNewGame = { gameState = GameState.initial(s.config) },
                onBackToConfig = { screen = AppScreen.Config }
            )
        }
    }
}
