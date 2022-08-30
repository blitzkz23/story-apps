package com.naufaldystd.storyapps.ui.register

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
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
	private var _binding: FragmentRegisterBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
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

		// Set on click listener for all button
		binding.apply {
			ctaLogin.setOnClickListener {
				findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
			}
			btnRegister.setOnClickListener {
				actionRegister()
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun actionRegister() {
		binding.apply {
			val name = etNameText.text.toString()
			val email = etEmailText.text.toString()
			val password = etPasswordText.text.toString()
			loading.visibility = View.VISIBLE

			lifecycleScope.launch {
				registerViewModel.registerAccount(name, email, password)
					.observe(viewLifecycleOwner) { respond ->
						when (respond) {
							is Resource.Loading -> {
								binding.loading.visibility = View.GONE
							}
							is Resource.Success -> {
								binding.loading.visibility = View.GONE
								Toast.makeText(context, getString(R.string.reg_success_msg), Toast.LENGTH_SHORT).show()
								activity?.onBackPressed()
							}
							is Resource.Error -> {
								binding.loading.visibility = View.GONE
								Toast.makeText(context, getString(R.string.reg_failed_msg), Toast.LENGTH_SHORT).show()
							}
						}

					}
			}
		}
	}

}