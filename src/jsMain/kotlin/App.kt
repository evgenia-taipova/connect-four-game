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
    data class Game(val config: GameConfig, val initialState: GameState? = null) : AppScreen()
}

fun main() {
    renderComposable(rootElementId = "root") {
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
            GameScreen(
                state = gameState,
                onColumnClick = { col ->
                    gameState = GameEngine.dropPiece(gameState, col)
                    saveGameState(gameState)
                },
                onNewGame = {
                    gameState = GameState.initial(s.config)
                    saveGameState(gameState)
                },
                onBackToConfig = {
                    screen = AppScreen.Config
                }
            )
        }
    }
}
