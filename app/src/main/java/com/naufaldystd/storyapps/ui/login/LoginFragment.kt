package com.naufaldystd.storyapps.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
	private lateinit var user: UserModel
	private var _binding: FragmentLoginBinding? = null

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
			loading.visibility = View.VISIBLE

			lifecycleScope.launch {
				loginViewModel.loginAccount(email, password).observe(viewLifecycleOwner) { user ->
					when (user) {
						is Resource.Loading -> {
							loading.visibility = View.GONE
						}
						is Resource.Success -> {
							loading.visibility = View.GONE
							Toast.makeText(
								context,
								buildString {
									append(getString(R.string.login_welcome))
									append(" ")
									append(user.data?.name)
								},
								Toast.LENGTH_SHORT
							).show()
							user.data?.let { loginViewModel.logUser(it) }
							findNavController().navigate(R.id.action_loginFragment_to_storyFragment)
						}
						is Resource.Error -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								context,
								getString(R.string.login_failed_msg),
								Toast.LENGTH_SHORT
							).show()
						}
					}
				}
			}
		}
	}


}