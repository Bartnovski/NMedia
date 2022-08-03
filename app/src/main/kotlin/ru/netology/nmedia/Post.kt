package ru.netology.nmedia

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long = 1,
    val author: String = "Author",
    val content: String = "Default post",
    val published: String = "now",
    val likedByMe: Boolean = false,
    val likesAmount: Long = 0,
    val shared: Long = 0,
    val videoResource: String? = null
)