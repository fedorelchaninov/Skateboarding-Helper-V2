package cz.tryptafunk.skatehelp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cz.tryptafunk.skatehelp.common.enum.Difficulty
import cz.tryptafunk.skatehelp.screens.entity.Trick
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Query
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.TrickTable) }
    var selectedTrick by remember { mutableStateOf<Trick?>(null) }
    var tricks by remember { mutableStateOf<List<Trick>>(emptyList()) }

    LaunchedEffect(Unit) {
        tricks = getTricks()
    }

    when (currentScreen) {
        is Screen.TrickTable -> TrickTableScreen(
            tricks = tricks,
            onTrickClick = { trick ->
                selectedTrick = trick
                currentScreen = Screen.TrickDetails
            },
            onMarkDone = { trick ->
                markTrickAsDone(trick)
            },
            onFilterChange = { name, difficulty, isDone ->
            }
        )

        is Screen.TrickDetails -> TrickDetailsScreen(
            trick = selectedTrick!!,
            onBack = { currentScreen = Screen.TrickTable }
        )
    }
}


fun filterTricks(
    name: String?,
    difficulty: Difficulty?,
    isDone: Boolean?
): Unit {

}

suspend fun getTricks(): List<Trick> {
    val firebaseFirestore = Firebase.firestore
    try {
        val userResponse =
            firebaseFirestore.collection("TRICKS").get()
        return userResponse.documents.map {
            it.data()
        }
    } catch (e: Exception) {
        throw e
    }
}

fun markTrickAsDone(trick: Trick) {
    val firebaseFirestore = Firebase.firestore
    runBlocking {
        firebaseFirestore.collection("tricks")
            .document(trick.name.orEmpty())
            .update("isDone", trick.isDone)
    }
}

sealed class Screen {
    object TrickTable : Screen()
    object TrickDetails : Screen()
}
