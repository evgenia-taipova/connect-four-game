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
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

sealed class AppScreen {
    object Config : AppScreen()
    data class Game(val config: GameConfig, val initialState: GameState? = null) : AppScreen()
}

fun main() {
    renderComposable(rootElementId = "root") {
        Style(AppStyle)
        App()
    }
}

@Composable
fun App() {
    var screen by remember {
        mutableStateOf<AppScreen>(
            loadGameState()?.let { AppScreen.Game(it.config, it) } ?: AppScreen.Config
        )
    }

    when (val s = screen) {
        is AppScreen.Config -> {
            var hasSavedGame by remember { mutableStateOf(loadGameState() != null) }
            ConfigScreen(
                hasSavedGame = hasSavedGame,
                onStart = { config ->
                    clearGameState()
                    screen = AppScreen.Game(config)
                },
                onResume = {
                    loadGameState()?.let { screen = AppScreen.Game(it.config, it) }
                },
                onClearSave = {
                    clearGameState()
                    hasSavedGame = false
                }
            )
        }
        is AppScreen.Game -> {
            var gameState by remember(s) {
                mutableStateOf(s.initialState ?: GameState.initial(s.config))
            }
            var lastDrop by remember { mutableStateOf<Pair<Int, Int>?>(null) }

            GameScreen(
                state = gameState,
                lastDrop = lastDrop,
                onColumnClick = { col ->
                    val prev = gameState
                    gameState = GameEngine.dropPiece(prev, col)
                    saveGameState(gameState)
                    val row = (0 until gameState.config.rows).firstOrNull { r ->
                        gameState.board[r][col] != prev.board[r][col]
                    }
                    lastDrop = row?.let { it to col }
                    window.setTimeout({ lastDrop = null }, 400)
                },
                onNewGame = {
                    gameState = GameState.initial(s.config)
                    saveGameState(gameState)
                    lastDrop = null
                },
                onBackToConfig = {
                    screen = AppScreen.Config
                }
            )
        }
    }
}
