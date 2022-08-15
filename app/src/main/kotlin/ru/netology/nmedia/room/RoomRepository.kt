package ru.netology.nmedia.room

import androidx.lifecycle.map
import ru.netology.nmedia.Post
import ru.netology.nmedia.Repository
import ru.netology.nmedia.db.PostDAO
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel

class RoomRepository(
    private val dao: PostDAO
) : Repository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun like(postId: Long) = dao.likedById(postId)

    override fun share(postId: Long) = dao.share(postId)

    override fun delete(postId: Long) = dao.delete(postId)

    override fun save(post: Post) = dao.save(post.toEntity())


}