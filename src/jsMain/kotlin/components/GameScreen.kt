package components

import androidx.compose.runtime.Composable
import game.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun GameScreen(
    state: GameState,
    lastDrop: Pair<Int, Int>? = null,
    onColumnClick: (Int) -> Unit,
    onNewGame: () -> Unit,
    onBackToConfig: () -> Unit
) {
    Div(attrs = {
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
            property("padding", "clamp(8px, 4vw, 32px)")
            property("font-family", "Arial, sans-serif")
        }
    }) {
        H1(attrs = {
            style { marginBottom(16.px) }
        }) {
            Text("Connect Four")
        }

        val statusText = when (val s = state.status) {
            is GameStatus.Playing -> when (state.currentPlayer) {
                Player.RED -> "Red's turn"
                Player.YELLOW -> "Yellow's turn"
            }
            is GameStatus.Win -> "${s.player.name.lowercase().replaceFirstChar { it.uppercaseChar() }} wins!"
            is GameStatus.Draw -> "It's a draw!"
        }

        val statusColor = when (state.status) {
            is GameStatus.Playing -> when (state.currentPlayer) {
                Player.RED -> Color("#F44336")
                Player.YELLOW -> Color("#F9A825")
            }
            else -> Color("#333333")
        }

        Div(attrs = {
            style {
                fontSize(22.px)
                property("font-weight", "bold")
                marginBottom(20.px)
                color(statusColor)
            }
        }) {
            Text(statusText)
        }

        Board(state = state, lastDrop = lastDrop, onColumnClick = onColumnClick)

        Div(attrs = {
            style {
                display(DisplayStyle.Flex)
                property("gap", "12px")
                marginTop(24.px)
            }
        }) {
            Button(attrs = {
                style {
                    padding(12.px)
                    fontSize(16.px)
                    property("cursor", "pointer")
                    backgroundColor(Color("#1565C0"))
                    color(Color("#ffffff"))
                    property("border", "none")
                    borderRadius(6.px)
                }
                onClick { onNewGame() }
            }) {
                Text("New Game")
            }

            Button(attrs = {
                style {
                    padding(12.px)
                    fontSize(16.px)
                    property("cursor", "pointer")
                    backgroundColor(Color("#546E7A"))
                    color(Color("#ffffff"))
                    property("border", "none")
                    borderRadius(6.px)
                }
                onClick { onBackToConfig() }
            }) {
                Text("Back to Settings")
            }
        }
    }
}
