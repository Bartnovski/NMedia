package ru.netology.nmedia.db

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.Repository

class SQLiteRepository(
    val dao: PostDAO
) : Repository {

    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }

    override val data: MutableLiveData<List<Post>> = MutableLiveData(dao.getAll())

    init {
        if (!data.value.isNullOrEmpty()) currentId = data.value!!.size.toLong()
    }


    override fun like(post: Post) {

        data.value = posts.map { postInList ->
            if (postInList.id == post.id) {
                if (!postInList.likedByMe) {
                     postInList.copy(
                        likedByMe = !(postInList.likedByMe),
                        likesAmount = (postInList.likesAmount + 1)
                    )
                } else postInList.copy(
                    likedByMe = !(postInList.likedByMe),
                    likesAmount = (postInList.likesAmount - 1)
                )
            }
            else postInList
        }
        dao.likedById(post.id)
    }

    override fun share(post: Post) {

        data.value = posts.map { post ->
            if (post.id == post.id) post.copy(shared = post.shared + 1)
            else post
        }
        dao.share(post)
    }

    override fun delete(postId: Long) {
        dao.removeById(postId)
        data.value = posts.filter { it.id != postId }
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        data.value = if (id == 0L) {
            listOf(post.copy(id = currentId)) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
    }

    companion object {
        var currentId = 0L
    }
}
