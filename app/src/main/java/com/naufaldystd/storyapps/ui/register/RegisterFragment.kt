package com.naufaldystd.storyapps.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
	private var _binding: FragmentRegisterBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentRegisterBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Set on click listener for all button
		binding.apply {
			ctaLogin.setOnClickListener {
				findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
			}
		}
	}

}