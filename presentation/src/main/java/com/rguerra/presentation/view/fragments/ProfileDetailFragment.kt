package com.rguerra.presentation.view.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rguerra.presentation.R
import com.rguerra.presentation.models.ProfileUi
import com.rguerra.presentation.view.dialogfragments.DialogFragmentCallback
import com.rguerra.presentation.view.dialogfragments.DialogVo
import com.rguerra.presentation.view.dialogfragments.RGDialogFragment
import com.rguerra.presentation.viewmodel.SelectedProfileSharedViewModel
import kotlinx.android.synthetic.main.fragment_profile_detail.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ProfileDetailFragment : Fragment() {

    private val viewModelSelectedProfile: SelectedProfileSharedViewModel by sharedViewModel()

    companion object {
        fun newInstance() = ProfileDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_profile_detail, container, false)

    override fun onStart() {
        super.onStart()
        viewModelSelectedProfile.selectedRepo.observe(this, Observer { showRepoDetail(it) })

        ic_neutral_profile.setOnClickListener {
            fragmentManager?.run {
                RGDialogFragment.newInstance(DialogVo(
                        title = R.string.dialog_title,
                        message = R.string.dialog_description,
                        messageOnPositiveBtn = R.string.ok,
                        messageOnNegativeBtn = R.string.dismiss)).also {

                    it.show(this, object : DialogFragmentCallback {
                        override fun onDialogNegativeClickListener() {
                            it.dismiss()
                        }

                        override fun onDialogPositiveClickListener() {
                            it.dismiss()
                        }
                    })
                }
            }
        }
    }

    private fun showRepoDetail(profileUi: ProfileUi) {
        tv_profile_name.text = profileUi.name
        tv_profile_company.text = getString(R.string.profile_detail_company, profileUi.company)
    }
}

interface OnDetailCallback {
    fun onNewPost(profileUi: ProfileUi?)
}
