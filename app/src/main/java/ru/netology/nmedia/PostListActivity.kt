package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.PostListBinding

class PostListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postListBinding = PostListBinding.inflate(layoutInflater)
        setContentView(postListBinding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(onShareClicked = { post ->
            viewModel.onLikeClicked(post)
        }, onLikeClicked = { post ->
            viewModel.onShareClicked(post)
        })
        postListBinding.postRecycler.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }

    fun render(amount: Long) = when (amount) {
        in 0..999 -> "$amount"
        in 1000..1099 -> "1K"
        in 1100..9999 -> "${(amount / 100).toDouble() / 10}K"
        in 10_000..999_999 -> "${amount / 1000}K"
        in 1_000_000..1_099_999 -> "1M"
        else -> "${(amount / 100_000).toDouble() / 10}M"
    }
}


