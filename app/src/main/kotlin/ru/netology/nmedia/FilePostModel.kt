package ru.netology.nmedia

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.properties.Delegates

class FilePostModel(private val application: Application) : Repository{

    override val data: MutableLiveData<List<Post>>
    private val prefs = application.getSharedPreferences(
        "repository", Context.MODE_PRIVATE
    )
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java,Post::class.java).type

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use { gson.fromJson(it,type) }
        } else {
            List(GENERATED_POST_AMOUNT) { index ->
                Post(
                    id = index + 1L,
                    author = "Нетология. Университет будущего.",
                    content = "Пост №${index + 1}\n" +
                            "Привет! Это новая Нетология! Когда-то Нетология начиналась с интенсивов" +
                            "по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике" +
                            "и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных" +
                            "профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть" +
                            "сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша" +
                            " миссия - помочь встать на путь роста и начать цепочку перемен →",
                    published = "26 мая в 18:36",
                    likesAmount = 999,
                    shared = 990,
                    likedByMe = false,
                    videoResource = "https://www.youtube.com/watch?v=xOgT2qYAzds&ab_channel=%D0%9D%D0%B5%D1%82%D0%BE%D0%BB%D0%BE%D0%B3%D0%B8%D1%8F"
                )
            }
        }
        data = MutableLiveData(posts)
    }

    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID,0L)
    ) { _,_,nextValue ->
        prefs.edit { putLong(NEXT_ID,nextValue)}

    }

    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            application.openFileOutput(
                FILE_NAME,Context.MODE_PRIVATE
            ).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }


    override fun like(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId)
                if (!post.likedByMe) post.copy(
                    likedByMe = !post.likedByMe,
                    likesAmount = post.likesAmount + 1
                )
                else post.copy(
                    likedByMe = !post.likedByMe,
                    likesAmount = post.likesAmount - 1
                )
            else post
        }
    }

    override fun share(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) post.copy(shared = post.shared + 1)
            else post
        }
    }

    override fun delete(postId: Long) {
        posts = posts.filter { it.id != postId }
    }

    override fun save(post: Post) {
        if (post.id == Repository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        posts = posts + post.copy(
            id = ++nextId
        )
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private companion object {
        const val GENERATED_POST_AMOUNT = 10
        const val NEXT_ID = "nextId"
        const val FILE_NAME = "posts.json"
    }
}
