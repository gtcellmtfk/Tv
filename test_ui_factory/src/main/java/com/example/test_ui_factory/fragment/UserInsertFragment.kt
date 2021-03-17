package com.example.test_ui_factory.fragment

import android.os.Bundle
import android.view.View
import com.bytebyte6.common.BaseFragment
import com.example.test_ui_factory.R
import com.example.test_ui_factory.databinding.FragmentUserDetailBinding
import com.example.test_ui_factory.viewmodel.UserViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class UserInsertFragment : BaseFragment<FragmentUserDetailBinding>(R.layout.fragment_user_insert){

    companion object {
        const val TAG = "UserListFragment"
        fun newInstance(): UserInsertFragment {
            return UserInsertFragment().apply {
                arguments = Bundle()
            }
        }
    }

    private val viewModel by viewModel<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {

        }
    }

    override fun initViewBinding(view: View): FragmentUserDetailBinding? {
        return FragmentUserDetailBinding.bind(view)
    }
}