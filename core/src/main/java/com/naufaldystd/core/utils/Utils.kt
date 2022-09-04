package com.naufaldystd.core.utils

import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Set TextView text attribute to locale date format
 *
 * @param datetime Timestamp
 */
fun TextView.setLocalDateFormat(datetime: String) {
	val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
	val date = sdf.parse(datetime) as Date

	val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date)
	this.text = formattedDate
}