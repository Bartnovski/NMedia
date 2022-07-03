package ru.netology.nmedia

import androidx.lifecycle.ViewModel
import java.time.temporal.TemporalAmount

open class PostViewModel : ViewModel() {

    private val repository : Repository = PostModel()
    val data = repository.data
    fun onLikeClicked(post: Post) = repository.like(post.id)
    fun onShareClicked(post: Post) = repository.share(post.id)
}