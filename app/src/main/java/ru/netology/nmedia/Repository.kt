package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface Repository {
    val data : LiveData<List<Post>>
    fun like(postId : Long)
    fun share(postId: Long)
}