package cz.tryptafunk.skatehelp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cz.tryptafunk.skatehelp.common.enum.Difficulty
import cz.tryptafunk.skatehelp.screens.entity.Trick
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
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
            }
        )

        is Screen.TrickDetails -> TrickDetailsScreen(
            trick = selectedTrick!!,
            onBack = { currentScreen = Screen.TrickTable }
        )
    }
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
        val querySnapshot = firebaseFirestore.collection("TRICKS")
            .where { "name" equalTo trick.name!! }
            .get()
            .documents[0]
        querySnapshot.reference.update(Pair("isDone", trick.isDone))
    }
}

sealed class Screen {
    object TrickTable : Screen()
    object TrickDetails : Screen()
}
