package ru.netology.nmedia


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.utils.SingleLiveEvent


open class PostViewModel : ViewModel(),PostInteractionListener {

    val repository : Repository = PostModel()
    val data = repository.data
    val currentPost = (MutableLiveData<Post?>(null))
    val shareEvent = SingleLiveEvent<Post>()
    val editEvent = SingleLiveEvent<Post>()
    val playVideoEvent = SingleLiveEvent<Post>()

    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post){
        shareEvent.value = post
        repository.share(post.id)
    }
    override fun onDeleteClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
        editEvent.value = post
    }

    override fun onPlayClicked(post: Post) {
       playVideoEvent.value = post
    }

    fun onCreateNewPost(content: String) {
        val post = currentPost.value?.copy(
            content = content
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