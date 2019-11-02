package com.rguerra.presentation.view.dialogfragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.rguerra.presentation.R
import kotlinx.android.parcel.Parcelize

class RGDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "RGDialogFragment"
        const val BUNDLE_DIALOG_VO = "BUNDLE_DIALOG_VO"

        fun newInstance(dialogVo: DialogVo) = RGDialogFragment().also {
            it.arguments = Bundle().also { bundle ->
                bundle.putParcelable(BUNDLE_DIALOG_VO, dialogVo)
            }
        }
    }

    private var dialogCallback: DialogFragmentCallback? = null

    override fun onCreateDialog(savedInstanceState: Bundle?) =
            buildDialog(arguments?.getParcelable(BUNDLE_DIALOG_VO)).create()

    private fun buildDialog(dialogVo: DialogVo?) = AlertDialog.Builder(
            activity,
            R.style.AppTheme_AlertDialog_Dialog
    ).also { builder ->
        dialogVo?.apply {
            builder.setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(messageOnPositiveBtn) { _, _ ->
                        dialogCallback?.onDialogPositiveClickListener()
                    }
                    .setNegativeButton(messageOnNegativeBtn) { _, _ ->
                        dialogCallback?.onDialogNegativeClickListener()
                    }
        }
    }

    override fun onDetach() {
        dialogCallback = null
        super.onDetach()
    }

    fun show(
            fragmentManager: FragmentManager,
            dialogCallback: DialogFragmentCallback
    ) {
        this.dialogCallback = dialogCallback
        show(fragmentManager, TAG)
    }
}

interface DialogFragmentCallback {
    fun onDialogNegativeClickListener()
    fun onDialogPositiveClickListener()
}

@Parcelize
data class DialogVo(
        @StringRes val title: Int,
        @StringRes val message: Int,
        @StringRes val messageOnPositiveBtn: Int,
        @StringRes val messageOnNegativeBtn: Int
) : Parcelable