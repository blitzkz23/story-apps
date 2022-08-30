package com.naufaldystd.storyapps.utils.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.naufaldystd.storyapps.R

class CustomPasswordField : AppCompatEditText {
	private lateinit var passwordIconDrawable: Drawable

	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		init()
	}

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)

		transformationMethod = PasswordTransformationMethod.getInstance()
		hint = context.getString(R.string.prompt_hint_password)
		background = context.getDrawable(R.drawable.edit_text_bg)
	}

	private fun init() {
		textAlignment = View.TEXT_ALIGNMENT_VIEW_START
		passwordIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_lock) as Drawable
		inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
		compoundDrawablePadding = 16
		setButtonDrawables(passwordIconDrawable)
		setAutofillHints(AUTOFILL_HINT_PASSWORD)

		addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				if (p0.toString().length < 6 && !p0.isNullOrEmpty()) setErrorMsg()
			}

			override fun afterTextChanged(p0: Editable?) { }
		})
	}

	private fun setButtonDrawables(
		startOfTheText: Drawable? = null,
		topOfTheText: Drawable? = null,
		endOfTheText: Drawable? = null,
		bottomOfTheText: Drawable? = null
	) {
		setCompoundDrawablesWithIntrinsicBounds(
			startOfTheText,
			topOfTheText,
			endOfTheText,
			bottomOfTheText
		)
	}

	private fun setErrorMsg() {
		error = context.getString(R.string.error_message_password)
	}

}