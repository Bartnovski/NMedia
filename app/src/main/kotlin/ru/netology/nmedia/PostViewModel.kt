package ru.netology.nmedia


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.db.SQLiteRepository

import ru.netology.nmedia.utils.SingleLiveEvent


open class PostViewModel(
    application: Application
) : AndroidViewModel(application),PostInteractionListener {

    private val repository: Repository = SQLiteRepository(
        dao = AppDb.getInstance(
            context = application
        ).postDAO
    )
    val data = repository.data
    private val currentPost = (MutableLiveData<Post?>(null))
    val shareEvent = SingleLiveEvent<Post>()
    val editEvent = SingleLiveEvent<Post>()
    val playVideoEvent = SingleLiveEvent<Post>()
    val onContentClickEvent = SingleLiveEvent<Post>()
    val onDeleteClickedEvent = SingleLiveEvent<Post>()

    override fun onLikeClicked(post: Post) = repository.like(post)
    override fun onShareClicked(post: Post){
        shareEvent.value = post
        repository.share(post)
    }
    override fun onDeleteClicked(post: Post) {
        onDeleteClickedEvent.value = post
        repository.delete(post.id)
    }
    override fun onEditClicked(post: Post) {
        editEvent.value = post
        currentPost.value = post
    }

    override fun onPlayClicked(post: Post) {
       playVideoEvent.value = post
    }

    override fun showDetailedView(post: Post) {
       onContentClickEvent.value = post
    }

    fun onCreateNewPost(content: String) {
        val post = currentPost.value?.copy(
            content = content,
        ) ?: Post(
            id = Repository.NEW_POST_ID,
            author = "Someone",
            content = content,
            published = "today",
            likesAmount = 1_299_999,
            shared = 999,
            videoResource = "https://www.youtube.com"
        )
        repository.save(post)
        currentPost.value = null
    }
}