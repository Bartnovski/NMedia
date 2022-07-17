package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostListActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding

class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post,PostsAdapter.ViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater,parent,false)  //Should be false
        return ViewHolder(binding,interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val renderAmount = PostListActivity()
        private lateinit var post : Post
        private val popupMenu by lazy {
            PopupMenu(itemView.context,binding.options).apply {
                inflate(R.menu.options)
                setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.remove -> {
                            listener.onDeleteClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likesPic.setOnClickListener {
                listener.onLikeClicked(post)
            }
            binding.sharePic.setOnClickListener {
                listener.onShareClicked(post)
            }
        }

        fun bind(post : Post) = with(binding) {
            this@ViewHolder.post = post
            authorName.text = post.author
            date.text = post.published
            content.text = post.content
            sharePic.text = renderAmount.render(post.shared)
            likesPic.text = renderAmount.render(post.likesAmount)
            likesPic.isChecked = post.likedByMe
            options.setOnClickListener { popupMenu.show() }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
           oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}