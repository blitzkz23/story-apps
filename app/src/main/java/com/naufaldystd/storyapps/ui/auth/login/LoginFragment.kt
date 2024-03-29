package com.naufaldystd.storyapps.ui.auth.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentLoginBinding
import com.naufaldystd.storyapps.ui.story.StoryActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
	private var _binding: FragmentLoginBinding? = null

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

		setupAnimation()
		setButtonEnable()
		setupButtonAction()
		binding.etPasswordText.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				setButtonEnable()
			}

			override fun afterTextChanged(s: Editable?) {}
		})

	}

	private fun setupAnimation() {
		ObjectAnimator.ofFloat(binding.ivLoginImage, View.TRANSLATION_X, -50f, 50f).apply {
			duration = 6000
			repeatCount = ObjectAnimator.INFINITE
			repeatMode = ObjectAnimator.REVERSE
		}.start()
	}

	/**
	 * Set click listener for all button
	 *
	 */
	private fun setupButtonAction() {
		binding.apply {
			ctaRegister.setOnClickListener {
				findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
			}
			btnLogin.setOnClickListener {
				actionLogin()
			}
			btnLoginGuest.setOnClickListener {
				startActivity(Intent(activity, StoryActivity::class.java).also { intent ->
					intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
				})
				activity?.finish()
			}
		}
	}

	/**
	 * Set validation for button state, only enable button if form are not empty and formats are correct
	 *
	 */
	private fun setButtonEnable() {
		val email = binding.etEmailText.text
		val password = binding.etPasswordText.text

		binding.btnLogin.isEnabled =
			(email != null) && Patterns.EMAIL_ADDRESS.matcher(email)
				.matches() && (password != null) && password.toString().length >= 6
	}

	/**
	 * Get text and password from client and send login account request to API
	 *
	 */
	private fun actionLogin() {
		binding.apply {
			val email = etEmailText.text.toString()
			val password = etPasswordText.text.toString()

			loading.visibility = View.VISIBLE
			lifecycleScope.launch {
				loginViewModel.loginAccount(email, password).collect { user ->
					when (user) {
						is Resource.Loading -> loading.visibility = View.VISIBLE
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}