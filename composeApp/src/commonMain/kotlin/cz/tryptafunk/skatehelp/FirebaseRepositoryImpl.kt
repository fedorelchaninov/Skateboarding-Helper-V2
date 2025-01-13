package cz.tryptafunk.skatehelp

import cz.tryptafunk.skatehelp.entity.Trick
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FirebaseRepositoryImpl : FirebaseRepository {

    private val firestore = Firebase.firestore
    private val _tricks = MutableStateFlow<List<Trick>>(emptyList())

    private val scope = CoroutineScope(SupervisorJob())

    override fun initialize() {
        scope.launch {
            fetchTricks()
        }
    }

    override fun observeTricks(): Flow<List<Trick>> = _tricks

    override suspend fun fetchTricks() {
        _tricks.value = firestore.collection("TRICKS").get().documents.map { it.data() }
    }

    override suspend fun changeDone(trick: Trick) {
        try {
            val querySnapshot = firestore.collection("TRICKS")
                .where { "name" equalTo trick.name!! }
                .get()

            val document = querySnapshot.documents.firstOrNull()
            if (document != null) {
                document.reference.update(Pair("isDone", !trick.isDone))
            } else {
                println("Trick with name ${trick.name} not found.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}