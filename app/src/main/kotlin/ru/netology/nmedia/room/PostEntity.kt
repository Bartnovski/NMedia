package ru.netology.nmedia.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
class PostEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "published")
    val published: String,

    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean,

    @ColumnInfo(name = "likes")
    val likesAmount: Long,

    @ColumnInfo(name = "shared")
    val shared: Long,

    @ColumnInfo(name = "videoResource")
    val videoResource: String
    )