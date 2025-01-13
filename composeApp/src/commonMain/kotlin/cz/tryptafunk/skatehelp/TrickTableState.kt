package cz.tryptafunk.skatehelp

import cz.tryptafunk.skatehelp.entity.Trick

data class TrickTableState(
    val tricks: List<Trick> = listOf(),
    val isRefreshing: Boolean = false
)
