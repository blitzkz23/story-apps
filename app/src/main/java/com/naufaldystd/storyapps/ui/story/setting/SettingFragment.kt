package com.naufaldystd.storyapps.ui.story.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.naufaldystd.storyapps.databinding.FragmentSettingBinding
import com.naufaldystd.storyapps.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
	private var _binding: FragmentSettingBinding? = null
	private val binding get() = _binding!!
	private val settingViewModel: SettingViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentSettingBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupUserInfo()
		setupButtonAction()
	}

	/**
	 * Set the user's name text if shared pref active, if not set to name as guest
	 *
	 */
	private fun setupUserInfo() {
		settingViewModel.getUser().observe(viewLifecycleOwner) { user ->
			binding.userName.text = user.name
		}
	}

	/**
	 * Set click listener for all button
	 *
	 */
	private fun setupButtonAction() {
		binding.settingLanguage.setOnClickListener {
			startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
		}
		binding.btnLogout.setOnClickListener {
			settingViewModel.logOutUser()
			val intent = Intent(requireActivity(), AuthActivity::class.java)
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(intent)
			activity?.finish()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}