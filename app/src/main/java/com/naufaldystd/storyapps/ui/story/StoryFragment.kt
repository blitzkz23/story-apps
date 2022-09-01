package com.naufaldystd.storyapps.ui.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.ui.StoryAdapter
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryFragment : Fragment() {
	private var _binding: FragmentStoryBinding? = null
	private val binding get() = _binding!!
	private val storyViewModel: StoryViewModel by viewModels()
	private lateinit var storyAdapter: StoryAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentStoryBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		storyAdapter = StoryAdapter()
		storyAdapter.onItemClick = {
			findNavController().navigate(R.id.action_storyFragment_to_detailStoryActivity)
		}

		storyViewModel.getUser().observe(viewLifecycleOwner) { user ->
			if (user.name == getString(R.string.guest)) {
				binding.messageForGuest.visibility = View.VISIBLE
				binding.btnRegister2.visibility = View.VISIBLE
				binding.btnRegister2.setOnClickListener {
					findNavController().navigate(R.id.action_storyFragment_to_registerFragment)
				}
			} else {
				setupHeaderTokenAndStoryData()
			}
		}

		getView()?.findViewById<ImageButton>(R.id.btn_setting)?.setOnClickListener {
			val navController = Navigation.findNavController(view)
			navController.navigate(R.id.action_storyFragment_to_settingActivity)
		}
	}

	private fun setupHeaderTokenAndStoryData() {
		storyViewModel.getUser().observe(viewLifecycleOwner) { user ->
			storyViewModel.getAllStories(user.token).observe(viewLifecycleOwner) { story ->
				if (story != null) {
					when (story) {
						is Resource.Loading -> binding.loading.visibility = View.GONE
						is Resource.Success -> {
							binding.loading.visibility = View.GONE
							storyAdapter.setData(story.data)
						}
						is Resource.Error -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								context,
								"Gagal menampilkan data, mohon coba lagi nanti",
								Toast.LENGTH_SHORT
							).show()
						}
					}
				}
			}
		}

		with(binding.rvStory) {
			layoutManager = LinearLayoutManager(context)
			setHasFixedSize(true)
			adapter = storyAdapter
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}