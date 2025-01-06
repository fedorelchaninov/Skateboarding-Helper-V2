package cz.tryptafunk.skatehelp.screens.entity

import kotlinx.serialization.Serializable

@Serializable
data class Trick(
    val name: String? = "",
    val difficulty: String? = "",
    val isDone: Boolean = false
)
