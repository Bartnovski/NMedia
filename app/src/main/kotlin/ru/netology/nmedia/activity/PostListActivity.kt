package ru.netology.nmedia.activity


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.PostListBinding


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

        val activityCreateEditPostLauncher = registerForActivityResult(
            NewPostActivity.ResultContractCreateEdit
        ){  postContent: String? ->
            postContent?.let(viewModel::onCreateNewPost)
        }

        postListBinding.addPostButton.setOnClickListener {
            activityCreateEditPostLauncher.launch("")
        }

        viewModel.editEvent.observe(this) {
            activityCreateEditPostLauncher.launch(it.content)
        }

        viewModel.playVideoEvent.observe(this) { post ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoResource))
            val goToResource = Intent.createChooser(intent,"")
            startActivity(goToResource)
        }

        viewModel.shareEvent.observe(this) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,post.content)
            }

            val shareIntent = Intent.createChooser(intent, getString(R.string.share))
            startActivity(shareIntent)
        }

    }

    companion object {
        fun render(amount: Long) = when (amount) {
            in 0..999 -> "$amount"
            in 1000..1099 -> "1K"
            in 1100..9999 -> "${(amount / 100).toDouble() / 10}K"
            in 10_000..999_999 -> "${amount / 1000}K"
            in 1_000_000..1_099_999 -> "1M"
            else -> "${(amount / 100_000).toDouble() / 10}M"
        }
    }
}

