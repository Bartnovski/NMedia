package ru.netology.nmedia.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.netology.nmedia.room.PostEntity

@Dao
interface PostDAO {

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll() : LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Update
    fun update(post: PostEntity)

    fun save(post: PostEntity) {
        if(post.id == 0L) insert(post) else update(post)
    }

    @Query(
        """
            UPDATE posts SET
            likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
            likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
            WHERE id = :postId
            """
    )
    fun likedById(postId: Long)

   @Query("DELETE FROM posts WHERE id = :postId")
   fun delete(postId: Long)

   @Query("UPDATE posts SET shared = shared + 1 WHERE id = :postId")
   fun share(postId: Long)
}
