package ru.netology.nmedia.db

import ru.netology.nmedia.Post
import ru.netology.nmedia.room.PostEntity

internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    likesAmount = likesAmount,
    likedByMe = likedByMe,
    shared = shared,
    videoResource = videoResource
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    likesAmount = likesAmount,
    likedByMe = likedByMe,
    shared = shared,
    videoResource = videoResource.toString()
)