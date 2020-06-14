package net.vizja.calculator

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

@BindingAdapter("mutableCalculationResult")
fun setMutableCalculationResult(view: TextView, text: MutableLiveData<CalculationResult>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value.toCalculationString()})
    }
}