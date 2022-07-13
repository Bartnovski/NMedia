package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.PostListBinding
import ru.netology.nmedia.utils.hideKeyboard

class PostListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postListBinding = PostListBinding.inflate(layoutInflater)
        setContentView(postListBinding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(viewModel)
        postListBinding.postRecycler.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        postListBinding.saveButton.setOnClickListener {
            with(postListBinding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
                postListBinding.editLayout.visibility = View.GONE
                clearFocus()
                hideKeyboard()
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->

            with(postListBinding.contentEditText) {
                val content = currentPost?.content
                setText(content)
                if (content != null) {
                    requestFocus()
                    postListBinding.editLayout.visibility = View.VISIBLE
                    postListBinding.editTextAuthorName.text = currentPost.author
                }
            }
        }

        postListBinding.closeButton.setOnClickListener {
            with(postListBinding.contentEditText) {
                postListBinding.editLayout.visibility = View.GONE
                viewModel.currentPost.value = null
                clearFocus()
                hideKeyboard()
            }
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

