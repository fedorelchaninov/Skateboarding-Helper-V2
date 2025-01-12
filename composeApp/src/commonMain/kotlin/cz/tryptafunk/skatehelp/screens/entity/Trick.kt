package cz.tryptafunk.skatehelp.screens.entity

import cz.tryptafunk.skatehelp.common.enum.Difficulty
import kotlinx.serialization.Serializable

@Serializable
data class Trick(
    val name: String? = "",
    val difficulty: Difficulty? = Difficulty.EASY,
    val description: String? = "",
    var isDone: Boolean = false,
    val youtubeLink: String? = ""
)
