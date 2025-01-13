package cz.tryptafunk.skatehelp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tryptafunk.skatehelp.entity.Trick
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrickTableViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            firebaseRepository.fetchTricks()
        }
    }

    private val _isRefreshing = MutableStateFlow(false)

    val screenState: StateFlow<TrickTableState> = firebaseRepository.observeTricks()
        .combine(_isRefreshing) { tricks, isRefreshing ->
            TrickTableState(tricks = tricks, isRefreshing = isRefreshing)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TrickTableState()
        )

    fun onPullToRefreshTrigger() {
        _isRefreshing.update { true }
        viewModelScope.launch {
            firebaseRepository.fetchTricks()
            _isRefreshing.update { false }
        }
    }

    fun onDoneCheckChange(trick: Trick) {
        viewModelScope.launch {
            firebaseRepository.changeDone(trick)
            firebaseRepository.fetchTricks()
        }
    }
}