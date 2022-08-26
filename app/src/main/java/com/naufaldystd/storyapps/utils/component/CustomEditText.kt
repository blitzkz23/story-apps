package com.naufaldystd.storyapps.utils.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.naufaldystd.storyapps.R

class CustomEditText : AppCompatEditText {
	private lateinit var visibleButtonImage: Drawable

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
		hint = "Silahkan masukkan password..."
		textAlignment = View.TEXT_ALIGNMENT_VIEW_START
		inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
		showHiddenButton()
	}

	private fun showVisibleButton() {
		setButtonDrawables(endOfTheText = visibleButtonImage)
	}

	private fun showHiddenButton() {
		visibleButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_hidden_eye) as Drawable
		setButtonDrawables(endOfTheText = visibleButtonImage)
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
		error = "Password minimal harus 6 kata"
	}

	private fun init() {
		visibleButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_hidden_eye) as Drawable
		addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				if (p0.toString().length < 6) setErrorMsg()
			}

			override fun afterTextChanged(p0: Editable?) {
				if (p0.toString().length < 6) setErrorMsg()
			}
		})
	}

}