package com.rguerra.profiles.view.activities

import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import androidx.lifecycle.Observer
import com.rguerra.presentation.R
import com.rguerra.presentation.view.activities.BaseActivity
import com.rguerra.presentation.viewmodel.SplashViewModel
import com.rguerra.profiles.view.utils.toggleVisibility
import kotlinx.android.synthetic.main.splash_activity_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

class SplashScreenActivity : BaseActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity_layout)

        tv_retry_msg.setOnClickListener {
            splashViewModel.onStart()
        }
    }

    override fun onStart() {
        super.onStart()
        splashViewModel.onStart()
        splashViewModel.networkConnection.observe(this, Observer { resource ->
            resource.fold(
                    onData = { showNetworkErrorScreen(it) }
            )
        })

        splashViewModel.result.observe(this, Observer { resource ->
            resource.fold(
                    onComplete = ::goToNextScreen,
                    onError = ::showErrorMessage
            )
        })
    }

    private fun showNetworkErrorScreen(visible: Boolean) {
        tv_retry_msg.toggleVisibility(visible)
        ic_wifi_connection.toggleVisibility(visible)
    }

    private fun goToNextScreen() = startActivity(MainActivity.newIntent(this)
            .also {
                it.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
            })

    override fun onStop() {
        splashViewModel.onStop()
        super.onStop()
    }
}
