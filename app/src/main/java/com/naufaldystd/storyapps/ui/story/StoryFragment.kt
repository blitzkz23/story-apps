package com.naufaldystd.storyapps.ui.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryFragment : Fragment() {
	private var _binding: FragmentStoryBinding? = null
	private val binding get() = _binding!!

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

		getView()?.findViewById<ImageButton>(R.id.btn_setting)?.setOnClickListener {
			val navController = Navigation.findNavController(view)
			navController.navigate(R.id.action_storyFragment_to_settingActivity)
		}
	}
}