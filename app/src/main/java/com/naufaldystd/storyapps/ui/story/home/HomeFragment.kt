package com.naufaldystd.storyapps.ui.story.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.ui.StoryAdapter
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentHomeBinding
import com.naufaldystd.storyapps.ui.detail.DetailStoryActivity
import com.naufaldystd.storyapps.ui.main.MainActivity
import com.naufaldystd.storyapps.ui.story.add.AddStoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!
	private val homeViewModel: HomeViewModel by viewModels()
	private lateinit var storyAdapter: StoryAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentHomeBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupButtonAction()
		setupAdapter()
		homeViewModel.getUser().observe(viewLifecycleOwner) { user ->
			if (user.name == getString(R.string.tamu) || user.name == getString(R.string.guest)) {
				with(binding) {
					imageSorry.visibility = View.VISIBLE
					messageForGuest.visibility = View.VISIBLE
					btnRegister2.visibility = View.VISIBLE
					rvStory.visibility = View.GONE
				}
			} else {
				setupHeaderTokenAndStoryData()
			}
		}
	}

	private fun setupButtonAction() {
		binding.btnAddStory.setOnClickListener {
			startActivity(Intent(requireActivity(), AddStoryActivity::class.java))
		}
		binding.btnRegister2.setOnClickListener {
			startActivity(Intent(requireActivity(), MainActivity::class.java).also { intent ->
				intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			})
			activity?.finish()
		}
	}

	/**
	 * Set adapter for recyclerview
	 *
	 */
	private fun setupAdapter() {
		storyAdapter = StoryAdapter()
		storyAdapter.onItemClick = { intentData ->
			val image =
				activity?.findViewById<ImageView>(com.naufaldystd.core.R.id.iv_item_image_list)
			val text =
				activity?.findViewById<TextView>(com.naufaldystd.core.R.id.tv_user_and_paragraph_list)
			val datetime =
				activity?.findViewById<TextView>(com.naufaldystd.core.R.id.tv_datetime_list)
			val optionsCompat: ActivityOptionsCompat =
				ActivityOptionsCompat.makeSceneTransitionAnimation(
					requireActivity(),
					Pair(image, "image"),
					Pair(text, "text"),
					Pair(datetime, "datetime")
				)
			val intent = Intent(requireActivity(), DetailStoryActivity::class.java)
			intent.putExtra(DetailStoryActivity.EXTRA_PARCEL, intentData)
			startActivity(intent, optionsCompat.toBundle())
		}
	}

	/**
	 * Set header token for data request and set the returned data into adapter and eventually recyclerview
	 */
	private fun setupHeaderTokenAndStoryData() {
		binding.loading.visibility = View.VISIBLE
		homeViewModel.getUser().observe(viewLifecycleOwner) { user ->
			homeViewModel.getAllStories(user.token).observe(viewLifecycleOwner) { story ->
				if (story != null) {
					when (story) {
						is Resource.Loading -> binding.loading.visibility = View.VISIBLE
						is Resource.Success -> {
							binding.loading.visibility = View.GONE
							storyAdapter.setData(story.data)
						}
						is Resource.Error -> {
							binding.loading.visibility = View.GONE
							binding.rvStory.visibility = View.VISIBLE
							Toast.makeText(
								activity,
								getString(R.string.fail_load_data),
								Toast.LENGTH_SHORT
							).show()
							with(binding) {
								binding.rvStory.visibility = View.GONE
								imageSorry.visibility = View.VISIBLE
								messageForGuest.visibility = View.VISIBLE
								messageForGuest.text = getString(R.string.data_not_available)
							}
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