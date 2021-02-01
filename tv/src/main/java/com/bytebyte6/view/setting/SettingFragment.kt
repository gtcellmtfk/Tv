package com.bytebyte6.view.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.bytebyte6.viewmodel.UserViewModel
import com.bytebyte6.common.BaseShareFragment
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentSettingBinding
import com.bytebyte6.view.setupOnBackPressedDispatcherBackToHome
import com.bytebyte6.view.setupToolbarMenuMode
import org.koin.android.viewmodel.ext.android.viewModel

class SettingFragment : BaseShareFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    companion object {
        const val TAG = "SettingFragment"
        fun newInstance(): SettingFragment {
            return SettingFragment().apply {
                arguments = Bundle()
            }
        }
    }

    private val viewModel: UserViewModel by viewModel()

    override fun initViewBinding(view: View): FragmentSettingBinding {
        return FragmentSettingBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarMenuMode(getString(R.string.nav_setting), "")
        setupOnBackPressedDispatcherBackToHome()

        binding?.apply {
            tvByteByte6.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bytebyte6")))
            }
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding?.swCapturePic?.isChecked = it.capturePic
            binding?.swNightMode?.isChecked = it.nightMode
            binding?.swNightMode?.setOnCheckedChangeListener { _, isChecked ->
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked)
                        AppCompatDelegate.MODE_NIGHT_YES
                    else
                        AppCompatDelegate.MODE_NIGHT_NO
                )
                viewModel.updateNight(isChecked)
            }
            binding?.swCapturePic?.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateCapturePic(isChecked)
            }
        })
    }
}