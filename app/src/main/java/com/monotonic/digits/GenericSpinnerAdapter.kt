package com.monotonic.digits

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.ArrayAdapter



/**
 * @author Ethan
 */
// Based on https://stackoverflow.com/a/8116756/2496050
class GenericSpinnerAdapter<T>(context: Context, textViewResourceId: Int, private val values: List<T>, private val titleExtractor: (T) -> String)
    : ArrayAdapter<T>(context, textViewResourceId, values) {

    override fun getCount(): Int = values.size
    override fun getItem(position: Int): T? = values[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = titleExtractor(values[position])

        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = titleExtractor(values[position])

        return label
    }
}