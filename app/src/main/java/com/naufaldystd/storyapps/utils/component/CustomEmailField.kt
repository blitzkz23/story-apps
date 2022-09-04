package com.naufaldystd.storyapps.utils.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.naufaldystd.storyapps.R

class CustomEmailField : AppCompatEditText {
	private lateinit var emailIconDrawable: Drawable

	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
		context,
		attrs,
		defStyleAttr
	) {
		init()
	}

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)

		hint = context.getString(R.string.prompt_hint_email)
		background = context.getDrawable(R.drawable.edit_text_bg)
	}

	private fun init() {
		textAlignment = View.TEXT_ALIGNMENT_VIEW_START
		emailIconDrawable = context.getDrawable(R.drawable.ic_email) as Drawable
		inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
		compoundDrawablePadding = 16
		setButtonDrawables(emailIconDrawable)
		setAutofillHints(AUTOFILL_HINT_PASSWORD)

		addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

			override fun afterTextChanged(s: Editable?) {
				if (!s?.let {
						Patterns.EMAIL_ADDRESS.matcher(it).matches()
					}!! && !s.isNullOrEmpty()) setErrorMsg()
			}
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
		error = context.getString(R.string.error_message_email)
	}
}