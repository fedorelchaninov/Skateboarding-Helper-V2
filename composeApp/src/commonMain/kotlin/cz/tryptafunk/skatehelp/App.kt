package cz.tryptafunk.skatehelp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import cz.tryptafunk.skatehelp.common.enum.Difficulty
import cz.tryptafunk.skatehelp.screens.entity.Trick
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.TrickTable) }
    var selectedTrick by remember { mutableStateOf<Trick?>(null) }
    var tricks by remember { mutableStateOf<List<Trick>>(emptyList()) }

    // Use a coroutine scope for asynchronous operations
    val coroutineScope = rememberCoroutineScope()

    // Fetch tricks when the composable is first launched
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
                coroutineScope.launch {
                    // Create an updated Trick instance with toggled isDone
                    val updatedTrick = trick.copy(isDone = !trick.isDone)

                    // Update Firestore
                    markTrickAsDone(updatedTrick)

                    // Update local state by replacing the old trick with the updated one
                    tricks = tricks.map {
                        if (it.name == updatedTrick.name) updatedTrick else it
                    }
                }
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

suspend fun markTrickAsDone(trick: Trick) {
    val firebaseFirestore = Firebase.firestore
    try {
        // Query Firestore for the trick with the given name
        val querySnapshot = firebaseFirestore.collection("TRICKS")
            .where { "name" equalTo trick.name }
            .get()
             // Suspend until the operation completes

        val document = querySnapshot.documents.firstOrNull()
        if (document != null) {
            // Update the isDone field in Firestore
            document.reference.update(Pair("isDone", trick.isDone))
        } else {
            // Handle the case where the trick isn't found
            println("Trick with name ${trick.name} not found.")
        }
    } catch (e: Exception) {
        // Handle exceptions appropriately
        e.printStackTrace()
    }
}


sealed class Screen {
    object TrickTable : Screen()
    object TrickDetails : Screen()
}
