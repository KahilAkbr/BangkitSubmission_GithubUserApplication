package com.example.mygithubapplication.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapplication.data.response.ItemsItem
import com.example.mygithubapplication.databinding.FragmentFollowBinding
import com.example.mygithubapplication.ui.adapter.UserAdapter

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val followViewModel by viewModels<FollowViewModel>()

    private var position: Int = 0
    private var username: String? = null

    companion object {
        const val ARG_POSITION = "position"
        const val USER_DATA = "user_data"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        followViewModel.user.observe(viewLifecycleOwner) { user ->
            getUser(user)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(USER_DATA)
        }
        if (position == 1) {
            followViewModel.getFollower(username)
            Log.d("FollowFragment", username!!)
        } else {
            followViewModel.getFollowing(username)
        }
    }

    private fun showLoading(isLoading: Boolean?) {
        binding.progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
        binding.notFound.visibility = if (isLoading == true) View.GONE else View.VISIBLE
    }

    private fun getUser(user: List<ItemsItem>) {
        if (user.isEmpty()) {
            binding.rvUser.visibility = View.GONE
            binding.notFound.visibility = View.VISIBLE
        } else {
            binding.rvUser.visibility = View.VISIBLE
            binding.notFound.visibility = View.GONE

            val adapter = UserAdapter()
            adapter.submitList(user)
            binding.rvUser.adapter = adapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
