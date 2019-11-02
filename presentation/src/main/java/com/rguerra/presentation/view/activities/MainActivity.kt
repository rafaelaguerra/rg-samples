package com.rguerra.profiles.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rguerra.presentation.R
import com.rguerra.presentation.models.ProfileUi
import com.rguerra.presentation.view.activities.PostsActivity
import com.rguerra.presentation.viewmodel.SelectedProfileSharedViewModel
import com.rguerra.presentation.view.fragments.OnDetailCallback
import com.rguerra.presentation.view.fragments.ProfileDetailFragment
import com.rguerra.presentation.view.fragments.ProfileListFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(),
        OnDetailCallback {

    private val selectedProfileSharedViewModel: SelectedProfileSharedViewModel by viewModel()

    companion object {
        const val ONLY_ONE_FRAGMENT = 1
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        initListFragment()
        title = getString(R.string.profile_list_activity_title)

        selectedProfileSharedViewModel.selectedRepo.observe(this, Observer {
            if (supportFragmentManager.fragments.size == ONLY_ONE_FRAGMENT) {
                initDetailFragment()
            }
        })
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putParcelable(ProfileListFragment.SELECTED_PROFILE, selectedProfileSharedViewModel.selectedRepo.value)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.getParcelable<ProfileUi>(ProfileListFragment.SELECTED_PROFILE)?.let {
            selectedProfileSharedViewModel.saveSelectedRepo(it)
        }
    }

    override fun onNewPost(profileUi: ProfileUi?) {
        startActivity(PostsActivity.newIntent(context = this, profileUi = profileUi))
    }

    private fun initDetailFragment() {
        supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.fl_container_detail, ProfileDetailFragment.newInstance())
                .commit()
    }

    private fun initListFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fl_container_list, ProfileListFragment.newInstance())
                .commit()
    }

}
