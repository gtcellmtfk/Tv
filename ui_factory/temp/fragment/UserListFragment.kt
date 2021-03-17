package com.example.test_ui_factory.fragment

import android.os.Bundle
import android.view.View
import com.bytebyte6.utils.ListFragment
import com.example.test_ui_factory.viewmodel.UserViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class UserListFragment : ListFragment(){

    companion object {
        const val TAG = "UserListFragment"
        fun newInstance(): UserListFragment {
            return UserListFragment().apply {
                arguments = Bundle()
            }
        }
    }

    private val viewModel by viewModel<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {

        }
    }

    override fun onLoadMore() {

    }

    override fun onRefresh() {

    }
}