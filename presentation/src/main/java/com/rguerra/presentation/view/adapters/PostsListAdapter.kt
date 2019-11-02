package com.rguerra.presentation.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rguerra.presentation.R
import com.rguerra.presentation.models.PostUi
import kotlinx.android.synthetic.main.item_post_list.view.*


class PostsListAdapter(private val callback: OnPostClickListener) :
        ListAdapter<PostUi, PostsListAdapter.PostViewHolder>(PostUiDiffCallback()) {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_post_list, parent, false))


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) = holder.bind(getItem(position), position)

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PostUi, position: Int) =
                with(itemView) {
                    tv_post_title.text = item.title
                    tv_post_body.text = item.body

                    animateEnter(this, position)
                }
    }

    private fun animateEnter(view: View, position: Int) {
        if (position > lastPosition) {
            view.startAnimation(AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left))
            lastPosition = position
        }
    }

    fun onItemDismiss(position: Int) {
        callback.onPostSwap(getItem(position))
    }

    class PostUiDiffCallback : DiffUtil.ItemCallback<PostUi>() {
        override fun areContentsTheSame(oldItem: PostUi, newItem: PostUi): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: PostUi, newItem: PostUi): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface OnPostClickListener {
        fun onPostSwap(item: PostUi)
    }
}

class PostSwipeItemCallback(private val adapter: PostsListAdapter?) : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled() = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) =
            ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter?.onItemDismiss(viewHolder.adapterPosition)
    }

    //drag feature not used
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

    override fun isLongPressDragEnabled() = false
}

