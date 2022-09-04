package com.naufaldystd.storyapps.utils.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.naufaldystd.storyapps.R

class CustomNameField : AppCompatEditText {
	private lateinit var personIconDrawable: Drawable

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

		hint = "Silakan masukkan nama..."
		background = context.getDrawable(R.drawable.edit_text_bg)
	}

	private fun init() {
		textAlignment = View.TEXT_ALIGNMENT_VIEW_START
		personIconDrawable = context.getDrawable(R.drawable.ic_person) as Drawable
		inputType = InputType.TYPE_CLASS_TEXT
		compoundDrawablePadding = 16
		setButtonDrawables(personIconDrawable)
		setAutofillHints(AUTOFILL_HINT_PASSWORD)

		addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

			override fun afterTextChanged(s: Editable?) {
				if (s.toString().isEmpty()) setErrorMsg()
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
		error = context.getString(R.string.error_message_name)
	}
}