package com.bytebyte6.view.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentSettingBinding
import com.bytebyte6.view.home.HomeFragment
import com.bytebyte6.view.replaceNotAddToBackStack
import com.bytebyte6.view.setupOnBackPressedDispatcher
import com.bytebyte6.view.setupToolbar
import org.koin.android.viewmodel.ext.android.viewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    companion object {
        const val TAG = "SettingFragment"
        fun newInstance(): SettingFragment {
            return SettingFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

    private val viewModel: UserViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupOnBackPressedDispatcher()
    }

    override fun initBinding(view: View): FragmentSettingBinding {
        return FragmentSettingBinding.bind(view).apply {
            setupToolbar(requireActivity(), getString(R.string.nav_setting), "")

            viewModel.user.observe(viewLifecycleOwner, Observer {
                swCapturePic.isChecked = it.capturePic
                swNightMode.isChecked = it.nightMode
                swNightMode.setOnCheckedChangeListener { _, isChecked ->
                    AppCompatDelegate.setDefaultNightMode(
                        if (isChecked)
                            AppCompatDelegate.MODE_NIGHT_YES
                        else
                            AppCompatDelegate.MODE_NIGHT_NO
                    )
                    viewModel.updateNight(isChecked)
                }
                swCapturePic.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.updateCapturePic(isChecked)
                }
            })

            tvByteByte6.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bytebyte6")))
            }
        }
    }
}