package components

import androidx.compose.runtime.*
import game.GameConfig
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun ConfigScreen(
    hasSavedGame: Boolean = false,
    onStart: (GameConfig) -> Unit,
    onResume: () -> Unit = {},
    onClearSave: () -> Unit = {}
) {
    var rows by remember { mutableStateOf(6) }
    var cols by remember { mutableStateOf(7) }
    var winCondition by remember { mutableStateOf(4) }

    val maxWin = minOf(rows, cols, 10)
    if (winCondition > maxWin) winCondition = maxWin

    val isValid = rows in 2..20 && cols in 2..20 && winCondition in 2..maxWin

    Div(attrs = {
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
            property("padding", "clamp(8px, 4vw, 32px)")
            property("font-family", "Arial, sans-serif")
        }
    }) {
        H1(attrs = { style { marginBottom(8.px) } }) {
            Text("Connect Four")
        }

        H2(attrs = { style { marginBottom(24.px); property("font-weight", "normal") } }) {
            Text("Game Settings")
        }

        ConfigField(label = "Rows", value = rows, min = 2, max = 20) { rows = it }
        ConfigField(label = "Columns", value = cols, min = 2, max = 20) { cols = it }
        ConfigField(label = "Win condition", value = winCondition, min = 2, max = maxWin) {
            winCondition = it
        }

        Div(attrs = {
            style {
                height(20.px)
                marginBottom(8.px)
                fontSize(14.px)
                color(Color("#F44336"))
            }
        }) {
            if (!isValid) {
                Text("Win condition must be between 2 and $maxWin")
            }
        }

        Button(attrs = {
            style {
                padding(12.px)
                fontSize(16.px)
                property("cursor", if (isValid) "pointer" else "not-allowed")
                backgroundColor(if (isValid) Color("#1565C0") else Color("#90A4AE"))
                color(Color("#ffffff"))
                property("border", "none")
                borderRadius(6.px)
            }
            if (!isValid) attr("disabled", "")
            onClick { if (isValid) onStart(GameConfig(rows, cols, winCondition)) }
        }) {
            Text("Start Game")
        }

        if (hasSavedGame) {
            Div(attrs = {
                style {
                    marginTop(24.px)
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    alignItems(AlignItems.Center)
                    property("gap", "8px")
                    padding(16.px)
                    borderRadius(8.px)
                    backgroundColor(Color("#F5F5F5"))
                    width(280.px)
                }
            }) {
                Div(attrs = { style { fontSize(14.px); color(Color("#666666")); marginBottom(4.px) } }) {
                    Text("You have a saved game")
                }
                Button(attrs = {
                    style {
                        width(100.percent)
                        padding(10.px)
                        fontSize(15.px)
                        property("cursor", "pointer")
                        backgroundColor(Color("#43A047"))
                        color(Color("#ffffff"))
                        property("border", "none")
                        borderRadius(6.px)
                    }
                    onClick { onResume() }
                }) {
                    Text("Resume Saved Game")
                }
                Button(attrs = {
                    style {
                        width(100.percent)
                        padding(8.px)
                        fontSize(13.px)
                        property("cursor", "pointer")
                        backgroundColor(Color("#ffffff"))
                        color(Color("#999999"))
                        property("border", "1px solid #dddddd")
                        borderRadius(6.px)
                    }
                    onClick { onClearSave() }
                }) {
                    Text("Clear Saved Game")
                }
            }
        }
    }
}

@Composable
private fun ConfigField(
    label: String,
    value: Int,
    min: Int,
    max: Int,
    onChange: (Int) -> Unit
) {
    Div(attrs = {
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.SpaceBetween)
            marginBottom(12.px)
            width(280.px)
        }
    }) {
        Label { Text("$label ($min–$max)") }
        Input(InputType.Number, attrs = {
            value(value.toString())
            attr("min", min.toString())
            attr("max", max.toString())
            style {
                width(80.px)
                padding(6.px)
                fontSize(16.px)
                borderRadius(4.px)
                property("border", "1px solid #ccc")
            }
            onInput { event ->
                event.value?.toInt()?.coerceIn(min, max)?.let { onChange(it) }
            }
        })
    }
}
