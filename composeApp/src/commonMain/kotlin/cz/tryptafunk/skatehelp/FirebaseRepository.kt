package cz.tryptafunk.skatehelp

import cz.tryptafunk.skatehelp.entity.Trick
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun initialize()

    fun observeTricks(): Flow<List<Trick>>

    suspend fun fetchTricks()

    suspend fun changeDone(trick: Trick)
}