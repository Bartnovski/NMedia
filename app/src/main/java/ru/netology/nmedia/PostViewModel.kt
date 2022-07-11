package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import java.time.temporal.TemporalAmount

open class PostViewModel : ViewModel(),PostInteractionListener {

    private val repository : Repository = PostModel()
    val data = repository.data
    val currentPost = MutableLiveData<Post?>(null)

    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) = repository.share(post.id)
    override fun onDeleteClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }

    fun onSaveButtonClicked(content: String){
        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = Repository.NEW_POST_ID,
            author = "Someone",
            content = content,
            published = "today",
            likesAmount = 1_299_999,
            shared = 999,
        )
        repository.save(post)
        currentPost.value = null
    }

}