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

    val maxWin = minOf(rows.coerceAtLeast(2), cols.coerceAtLeast(2), 10)
    if (winCondition > maxWin) winCondition = maxWin

    val rowsValid = rows in 2..20
    val colsValid = cols in 2..20
    val winValid = winCondition in 2..maxWin
    val isValid = rowsValid && colsValid && winValid

    Div(attrs = {
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
            property("padding", "clamp(8px, 4vw, 32px)")
            property("font-family", "'Onest', sans-serif")
            color(Color("#3d2314"))
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
                color(Color("#c0392b"))
            }
        }) {
            when {
                !rowsValid -> Text("Rows must be between 2 and 20")
                !colsValid -> Text("Columns must be between 2 and 20")
                !winValid -> Text("Win condition must be between 2 and $maxWin")
            }
        }

        Button(attrs = {
            style {
                padding(12.px)
                fontSize(16.px)
                property("font-family", "inherit")
                property("font-weight", "700")
                property("cursor", if (isValid) "pointer" else "not-allowed")
                backgroundColor(if (isValid) Color("#eca439") else Color("#d4c8ae"))
                color(Color("#3d2314"))
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
                    backgroundColor(Color("#eae2ce"))
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
                        property("font-family", "inherit")
                        property("font-weight", "600")
                        property("cursor", "pointer")
                        backgroundColor(Color("#eca439"))
                        color(Color("#3d2314"))
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
                        property("font-family", "inherit")
                        property("cursor", "pointer")
                        backgroundColor(Color("#faf8ef"))
                        color(Color("#888888"))
                        property("border", "1px solid #d4c8ae")
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
                property("font-family", "inherit")
                borderRadius(4.px)
                property("border", "1px solid #d4c8ae")
                backgroundColor(Color("#faf8ef"))
                color(Color("#3d2314"))
            }
            onInput { event ->
                event.value?.toInt()?.let { onChange(it) }
            }
        })
    }
}
