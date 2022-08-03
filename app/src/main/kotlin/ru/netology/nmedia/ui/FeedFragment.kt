package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.ui.NewPostFragment.Companion.textArg

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater,container,false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val adapter = PostsAdapter(viewModel)
        binding.postRecycler.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

       viewModel.onContentClickEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_feedFragment_to_postFragment)
        }

        binding.addPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        viewModel.editEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply { textArg = viewModel.editEvent.value?.content })
        }

        viewModel.playVideoEvent.observe(viewLifecycleOwner) { post ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoResource))
            val goToResource = Intent.createChooser(intent, "")
            startActivity(goToResource)
        }

        viewModel.shareEvent.observe(viewLifecycleOwner) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.content)

            }
            val shareIntent = Intent.createChooser(intent, getString(R.string.share))
            startActivity(shareIntent)
        }
        return binding.root
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
