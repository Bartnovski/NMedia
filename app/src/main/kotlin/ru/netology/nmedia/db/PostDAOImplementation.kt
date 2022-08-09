package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.Post

class PostDAOImplementation(private val db: SQLiteDatabase) : PostDAO {


    override fun getAll() = db.query(
        PostsTable.NAME,
        PostsTable.ALL_COLUMNS_NAMES,
        null, null, null, null,
        "${PostsTable.Column.ID.columnName} DESC"
    ).use { cursor ->
        List(cursor.count) {
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {

            put(PostsTable.Column.AUTHOR.columnName, post.author)
            put(PostsTable.Column.CONTENT.columnName, post.content)
            put(PostsTable.Column.PUBLISHED.columnName, post.published)
            put(PostsTable.Column.LIKES.columnName, post.likesAmount)
            put(PostsTable.Column.LIKED_BY_ME.columnName, if (post.likedByMe) 1 else 0)
            put(PostsTable.Column.SHARED.columnName, post.shared)
        }
        if (post.id != 0L) {
            values.put(PostsTable.Column.ID.columnName, post.id)
            db.update(
                PostsTable.NAME,
                values,
                "${PostsTable.Column.ID.columnName} = ?",
                arrayOf(post.id.toString())
            )
        } else {
            SQLiteRepository.currentId++
            values.put(PostsTable.Column.ID.columnName, SQLiteRepository.currentId)
            db.insert(PostsTable.NAME, null, values)

        }
        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(SQLiteRepository.currentId.toString()),
            null, null, null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun likedById(postId: Long) {
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                    ${PostsTable.Column.LIKES.columnName} = ${PostsTable.Column.LIKES.columnName} + 
                    CASE WHEN ${PostsTable.Column.LIKED_BY_ME.columnName} THEN -1 ELSE 1 END,
                    ${PostsTable.Column.LIKED_BY_ME.columnName} = 
                    CASE WHEN ${PostsTable.Column.LIKED_BY_ME.columnName} THEN 0 ELSE 1 END
                WHERE ${PostsTable.Column.ID.columnName} = ?;
            """.trimIndent(), arrayOf(postId)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }

    override fun share(post: Post) {
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                ${PostsTable.Column.SHARED.columnName} = ${post.shared + 1}
                WHERE ${PostsTable.Column.ID.columnName} = ${post.id};
            """.trimIndent()
        )
    }
}