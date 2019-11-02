package com.rguerra.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.rguerra.presentation.models.PostUi
import com.rguerra.presentation.models.ProfileUi
import com.rguerra.presentation.viewmodel.PostsViewModel
import com.rguerra.presentation.view.adapters.PostsListAdapter
import com.rguerra.presentation.view.adapters.PostSwipeItemCallback
import kotlinx.android.synthetic.main.new_post_activity_layout.*
import android.view.LayoutInflater
import com.rguerra.presentation.R
import com.rguerra.profiles.view.utils.toggleVisibility
import kotlinx.android.synthetic.main.dialog_new_post.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class PostsActivity : BaseActivity(),
        PostsListAdapter.OnPostClickListener {

    private val postsViewModel: PostsViewModel by viewModel()
    private var postsAdapter: PostsListAdapter? = null

    companion object {
        const val USER_ID_EXTRA = "USER_ID_EXTRA"
        fun newIntent(context: Context, profileUi: ProfileUi?): Intent =
                Intent(context, PostsActivity::class.java).putExtra(USER_ID_EXTRA, profileUi)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_post_activity_layout)
    }

    override fun onStart() {
        super.onStart()
        initView()

        postsViewModel.start(intent.getParcelableExtra(USER_ID_EXTRA))
        postsViewModel.data.observe(this, Observer { resource ->
            resource.fold(
                    onComplete = {},
                    onData = ::showPostsList,
                    onError = ::showErrorMessage
            )
        })

        postsViewModel.title.observe(this, Observer {
            title = it
        })

        postsViewModel.loading.observe(this, Observer { visible ->
            tv_loading.toggleVisibility(visible)
        })
    }

    private fun initView() {
        initNewPostDialog()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        postsAdapter = PostsListAdapter(callback = this)
        rc_posts.layoutManager = LinearLayoutManager(this)
        rc_posts.adapter = postsAdapter

        val touchHelper = ItemTouchHelper(PostSwipeItemCallback(postsAdapter))
        touchHelper.attachToRecyclerView(rc_posts)
    }

    private fun initNewPostDialog() {
        fb_add_new_post.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)

            with(LayoutInflater.from(this).inflate(R.layout.dialog_new_post, null)) {
                alertDialogBuilder
                        .setView(this)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok) { _, _ ->
                            postsViewModel.addNewPost(title = this.et_post_title.text.toString(), body = this.et_post_body.text.toString())
                        }.setNegativeButton(R.string.dismiss) { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
            }
        }
    }

    private fun showPostsList(posts: List<PostUi>) {
        postsAdapter?.let {
            it.submitList(posts)
            it.notifyDataSetChanged()
        }
    }

    override fun onPostSwap(item: PostUi) {
        postsViewModel.onPostRemove(item)
    }

    override fun onStop() {
        postsViewModel.onStop()
        super.onStop()
    }

}