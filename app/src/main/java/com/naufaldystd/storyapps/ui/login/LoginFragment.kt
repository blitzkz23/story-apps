package com.naufaldystd.storyapps.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
	private var _binding : FragmentLoginBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!
	private val loginViewModel: LoginViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentLoginBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Set on click listeners for all button
		binding.apply {
			ctaRegister.setOnClickListener {
				findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
			}
			btnLogin.setOnClickListener {
				actionLogin()
			}
			btnLoginGuest.setOnClickListener {
				findNavController().navigate(R.id.action_loginFragment_to_storyFragment)
			}
		}

	}

	private fun actionLogin() {
		binding.apply {
			val email = etEmailText.text.toString()
			val password = etPasswordText.text.toString()

			lifecycleScope.launch {
				loginViewModel.loginAccount(email, password)
			}
		}
	}


}