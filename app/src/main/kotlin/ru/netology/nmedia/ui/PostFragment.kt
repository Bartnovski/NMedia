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
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.ui.NewPostFragment.Companion.textArg

class PostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(layoutInflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val viewHolder = PostsAdapter.ViewHolder(binding.postLayout,viewModel)
        val post = viewModel.onContentClickEvent.value!!
        viewHolder.bind(post)

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            posts.forEach { postInData ->
                if (postInData.id == post.id) viewHolder.bind(postInData)
            }
        }

        viewModel.editEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_postFragment_to_newPostFragment,
                Bundle().apply { textArg = viewModel.editEvent.value?.content })
        }

        viewModel.onDeleteClickedEvent.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
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
}