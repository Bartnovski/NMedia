package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostModel : Repository {

    private var posts
    get() = checkNotNull(data.value)
    set(value) {
        data.value = value
    }

    override val data : MutableLiveData<List<Post>>

    init {
        val initialPosts = List(1000) { index ->
            Post(
                id = index + 1L,
                author = "Нетология. Университет будущего.",
                content = "Индекс поста ${index + 1}\n"+
                        "Привет! Это новая Нетология! Когда-то Нетология начиналась с интенсивов" +
                        "по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике" +
                        "и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных" +
                        "профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть" +
                        "сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша" +
                        "миссия - помочь встать на путь роста и начать цепочку перемен →",
                published = "26 мая в 18:36",
                likesAmount = 999,
                shared = 990,
                likedByMe = false
            )
        }
        data = MutableLiveData(initialPosts)
    }

    override fun like(postId : Long) {
        posts = posts.map { post ->
            if (post.id == postId)
                if(!post.likedByMe) post.copy(likedByMe = !post.likedByMe,
                    likesAmount = post.likesAmount + 1)
                else post.copy(likedByMe = !post.likedByMe,
                likesAmount = post.likesAmount - 1)
            else post
        }
    }

    override fun share(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) post.copy(shared = post.shared + 1)
            else post
        }
    }
}
