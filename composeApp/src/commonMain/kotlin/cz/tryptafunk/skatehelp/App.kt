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
import cz.tryptafunk.skatehelp.screens.entity.Trick
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

@Composable
fun App() {
    Column(Modifier.fillMaxWidth()) {
        var list by remember { mutableStateOf(listOf<Trick>()) }
        LaunchedEffect(Unit) {
            list = getTricks()
        }
        LazyColumn {
            items(list) {
                TrickItem(it)
            }
        }
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

@Composable
fun TrickItem(trick: Trick) {
    Column {
        trick.name?.let {
            Text(
                text = it
            )
        }
        trick.difficulty?.let {
            Text(
                text = it.toString()
            )
        }
        Text(
            text = trick.isDone.toString()
        )
        Text(
            text = trick.description.toString()
        )
        Text(
            text = trick.youtubeLink.toString()
        )
    }
}
