package com.naufaldystd.storyapps.ui.auth.register

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
	private var _binding: FragmentRegisterBinding? = null

	private val binding get() = _binding!!
	private val registerViewModel: RegisterViewModel by viewModels()

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
		ObjectAnimator.ofFloat(binding.ivRegisterImage, View.TRANSLATION_X, -50f, 50f).apply {
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
			ctaLogin.setOnClickListener {
				findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
			}
			btnRegister.setOnClickListener {
				actionRegister()
			}
		}
	}

	/**
	 * Set validation for button state, only enable button if form are not empty and formats are correct
	 *
	 */
	private fun setButtonEnable() {
		val name = binding.etNameText.text
		val email = binding.etEmailText.text
		val password = binding.etPasswordText.text

		binding.btnRegister.isEnabled =
			(name != null) && name.toString()
				.isNotEmpty() && (email != null) && Patterns.EMAIL_ADDRESS.matcher(email)
				.matches() && (password != null) && password.toString().length >= 6
	}

	/**
	 * Get name, text and password from client and send register account request to API
	 *
	 */
	private fun actionRegister() {
		binding.loading.visibility = View.VISIBLE

		val name = binding.etNameText.text.toString()
		val email = binding.etEmailText.text.toString()
		val password = binding.etPasswordText.text.toString()

		lifecycleScope.launch {
			registerViewModel.registerAccount(name, email, password)
				.collect { respond ->
					when (respond) {
						is Resource.Loading -> binding.loading.visibility = View.GONE
						is Resource.Success -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								context,
								getString(R.string.reg_success_msg),
								Toast.LENGTH_SHORT
							).show()
							activity?.onBackPressed()
						}
						is Resource.Error -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								context,
								getString(R.string.reg_failed_msg),
								Toast.LENGTH_SHORT
							).show()
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