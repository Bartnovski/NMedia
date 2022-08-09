package ru.netology.nmedia.db

import ru.netology.nmedia.Post

interface PostDAO {
    fun getAll() : List<Post>
    fun save(post: Post) : Post
    fun share(post: Post)
    fun likedById(postId: Long)
    fun removeById(id: Long)
}