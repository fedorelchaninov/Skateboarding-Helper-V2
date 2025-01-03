package cz.tryptafunk.skatehelp.screens.detail

import androidx.lifecycle.ViewModel
import cz.tryptafunk.skatehelp.data.MuseumObject
import cz.tryptafunk.skatehelp.data.MuseumRepository
import kotlinx.coroutines.flow.Flow

class DetailViewModel(private val museumRepository: MuseumRepository) : ViewModel() {
    fun getObject(objectId: Int): Flow<MuseumObject?> =
        museumRepository.getObjectById(objectId)
}
