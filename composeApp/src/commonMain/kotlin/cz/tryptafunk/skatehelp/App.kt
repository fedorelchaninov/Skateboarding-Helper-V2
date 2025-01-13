package cz.tryptafunk.skatehelp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cz.tryptafunk.skatehelp.entity.Trick

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.TrickTable) }
    var selectedTrick by remember { mutableStateOf<Trick?>(null) }

    when (currentScreen) {
        is Screen.TrickTable -> TrickTableScreen(
            onTrickClick = { trick ->
                selectedTrick = trick
                currentScreen = Screen.TrickDetails
            }
        )

        is Screen.TrickDetails -> TrickDetailsScreen(
            trick = selectedTrick!!,
            onBack = { currentScreen = Screen.TrickTable }
        )
    }
}

sealed class Screen {
    object TrickTable : Screen()
    object TrickDetails : Screen()
}
