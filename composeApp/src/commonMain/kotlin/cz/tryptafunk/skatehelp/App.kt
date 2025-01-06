package cz.tryptafunk.skatehelp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cz.tryptafunk.skatehelp.screens.detail.DetailScreen
import cz.tryptafunk.skatehelp.screens.entity.Trick
import cz.tryptafunk.skatehelp.screens.list.ListScreen
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.Serializable

@Serializable
object ListDestination

@Serializable
data class DetailDestination(val objectId: Int)

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
                text = it
            )
        }
        Text(
            text = trick.isDone.toString()
        )
    }
}
