package com.rguerra.profiles.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rguerra.presentation.R
import com.rguerra.presentation.models.ProfileUi
import kotlinx.android.synthetic.main.item_profile_list.view.*


class ProfileListAdapter(private val callback: OnProfilesClickListener) :
        ListAdapter<ProfileUi, ProfileListAdapter.ProfileViewHolder>(ProfilesUiDiffCallback()) {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProfileViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_profile_list, parent, false))


    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(getItem(position), callback, position)
    }

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ProfileUi, listener: OnProfilesClickListener,
                 position: Int) =
                with(itemView) {
                    tv_profile_name.text = item.name
                    tv_number_of_posts.text = item.numberPosts
                    tv_number_of_albums.text = item.numberAlbums

                    setOnClickListener {
                        listener.onUserItemClicked(item)
                    }

                    with(context) {
                        if (item.selected) {
                            v_selected_item.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
                        } else {
                            v_selected_item.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
                        }
                    }
                    animateEnter(itemView, position)
                }
    }

    private fun animateEnter(view: View, position: Int) {
        if (position > lastPosition) {
            view.startAnimation(AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left))
            lastPosition = position
        }
    }

    class ProfilesUiDiffCallback : DiffUtil.ItemCallback<ProfileUi>() {
        override fun areContentsTheSame(oldItem: ProfileUi, newItem: ProfileUi): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: ProfileUi, newItem: ProfileUi): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface OnProfilesClickListener {
        fun onUserItemClicked(id: ProfileUi)
    }
}

