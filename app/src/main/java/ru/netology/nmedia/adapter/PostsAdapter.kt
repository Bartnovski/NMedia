package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostListActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding

class PostsAdapter(
    private val onShareClicked: (Post) -> Unit,
    private val onLikeClicked : (Post) -> Unit,
) : ListAdapter<Post,PostsAdapter.ViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater,parent,false)  //Should be false
        return ViewHolder(binding,onLikeClicked,onShareClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostBinding,
        private val onShareClicked : (Post) -> Unit,
        private val onLikeClicked : (Post) -> Unit,

    ) : RecyclerView.ViewHolder(binding.root) {

        private val renderAmount = PostListActivity()
        private lateinit var post : Post

        init {
            binding.likesPic.setOnClickListener {
                onLikeClicked(post)
            }
            binding.sharePic.setOnClickListener {
                onShareClicked(post)
            }
        }

        fun bind(post : Post) = with(binding) {
            this@ViewHolder.post = post
            authorName.text = post.author
            date.text = post.published
            content.text = post.content
            likesAmount.text = renderAmount.render(post.likesAmount)
            sharesAmount.text = renderAmount.render(post.shared)
            likesPic.setImageResource(
                if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_likes_24
            )
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
           oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}