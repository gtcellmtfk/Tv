package com.bytebyte6.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bytebyte6.common.BaseFragment
import com.bytebyte6.view.databinding.FragmentAboutBinding

class AboutFragment : BaseFragment<FragmentAboutBinding>(R.layout.fragment_about) {

    companion object {
        const val TAG = "AboutFragment"
    }

    override fun initViewBinding(view: View): FragmentAboutBinding {
        return FragmentAboutBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarMenuMode(getString(R.string.about),"")
        setupOnBackPressedDispatcherBackToHome()
        binding?.tvByteByte6?.setOnClickListener {
            val parse = Uri.parse("https://github.com/bytebyte6")
            val intent = Intent(Intent.ACTION_VIEW, parse)
            startActivity(intent)
        }
    }
}