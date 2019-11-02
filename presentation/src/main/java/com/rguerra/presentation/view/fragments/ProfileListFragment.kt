package com.rguerra.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rguerra.presentation.GENERIC_ERROR
import com.rguerra.presentation.R
import com.rguerra.presentation.models.ProfileUi
import com.rguerra.presentation.viewmodel.ProfileListViewModel
import com.rguerra.presentation.viewmodel.SelectedProfileSharedViewModel
import com.rguerra.profiles.view.adapters.ProfileListAdapter
import com.rguerra.profiles.view.adapters.ProfileListAdapter.OnProfilesClickListener
import com.rguerra.profiles.view.utils.toggleVisibility
import com.rguerra.profiles.view.utils.visible
import kotlinx.android.synthetic.main.fragment_profile_list.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileListFragment : Fragment(),
        OnProfilesClickListener {

    private var profileListAdapter: ProfileListAdapter? = null
    private val profileListViewModel: ProfileListViewModel by viewModel()
    private val viewModelSelectedProfile: SelectedProfileSharedViewModel by sharedViewModel()
    private val selectedProfileSharedViewModel: SelectedProfileSharedViewModel by sharedViewModel()
    private var listener: OnDetailCallback? = null

    companion object {
        const val SELECTED_PROFILE = "SELECTED_PROFILE"
        fun newInstance() = ProfileListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_profile_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onStart() {
        super.onStart()

        profileListViewModel.loading.observe(this, Observer {
            showLoading(it)
        })

        profileListViewModel.loadData(selectedProfileSharedViewModel.selectedRepo.value)
        profileListViewModel.data.observe(this, Observer { resource ->
            resource.fold(
                    onData = ::showProfileList,
                    onError = ::showErrorMessage,
                    onComplete = {}
            )
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDetailCallback) {
            listener = context
        } else {
            throw IllegalStateException("Parent must implement listener interface")
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putParcelable(SELECTED_PROFILE, selectedProfileSharedViewModel.selectedRepo.value)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.getParcelable<ProfileUi>(SELECTED_PROFILE)?.let {
            selectedProfileSharedViewModel.saveSelectedRepo(it)
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onUserItemClicked(id: ProfileUi) {
        fb_add_post.visible()
        selectedProfileSharedViewModel.saveSelectedRepo(id)
        profileListViewModel.saveSelectedProfile(id)
    }

    private fun initView() {
        profileListAdapter = ProfileListAdapter(callback = this)
        context?.let {
            recycler_repo.layoutManager = LinearLayoutManager(it)
            recycler_repo.adapter = profileListAdapter
        }

        fb_add_post.setOnClickListener { listener?.onNewPost(viewModelSelectedProfile.selectedRepo.value) }
    }

    private fun showLoading(visible: Boolean) = tv_loading.toggleVisibility(visible)

    private fun showErrorMessage(errorMessage: String) {
        val msg = if (errorMessage == GENERIC_ERROR) {
            getString(R.string.generic_error)
        } else {
            errorMessage
        }

        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    private fun showProfileList(profiles: List<ProfileUi>?) {
        profileListAdapter?.let {
            it.submitList(profiles)
            it.notifyDataSetChanged()
        }
    }

    override fun onStop() {
        profileListAdapter?.submitList(null)
        profileListViewModel.onStop()
        super.onStop()
    }
}
