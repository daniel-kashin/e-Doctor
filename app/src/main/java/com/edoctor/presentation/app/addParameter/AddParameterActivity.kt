package com.edoctor.presentation.app.addParameter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.edoctor.R
import com.edoctor.data.entity.remote.model.record.*
import com.edoctor.data.entity.remote.model.record.BodyParameterType.Custom.Companion.NEW
import com.edoctor.utils.*
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import java.util.UUID.randomUUID

class AddParameterActivity : AppCompatActivity() {

    companion object {
        const val PARAMETER_TYPE_PARAM = "parameter_type"
        const val PARAMETER_PARAM = "parameter"
    }

    private val toolbar by lazyFind<Toolbar>(R.id.toolbar)

    private val dateEditText by lazyFind<AppCompatEditText>(R.id.date)
    private val timeEditText by lazyFind<AppCompatEditText>(R.id.time)
    private val nameEditText by lazyFind<AppCompatEditText>(R.id.name)
    private val unitEditText by lazyFind<AppCompatEditText>(R.id.unit)
    private val firstValueEditText by lazyFind<AppCompatEditText>(R.id.first_value)
    private val secondValueEditText by lazyFind<AppCompatEditText>(R.id.second_value)
    private val firstValueLayout by lazyFind<TextInputLayout>(R.id.first_value_layout)
    private val secondValueLayout by lazyFind<TextInputLayout>(R.id.second_value_layout)
    private val secondValueDelimiter by lazyFind<View>(R.id.second_value_delimiter)
    private val saveButton by lazyFind<Button>(R.id.save_button)

    private var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    private val timestamp: Long get() = calendar.timeInMillis.javaTimeToUnixTime()
    private val maxDate = calendar.timeInMillis

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_parameter)

        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
            title = getString(R.string.parameter)
        }

        dateEditText.setText(
            SimpleDateFormat("dd.MM.yyyy")
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
                .format(calendar.time)
        )
        timeEditText.setText(
            SimpleDateFormat("hh:mm")
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
                .format(calendar.time)
        )

        dateEditText.isFocusable = false
        dateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(
                        year,
                        month,
                        dayOfMonth,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        0
                    )
                    calendar.time
                    dateEditText.setText(
                        SimpleDateFormat("dd.MM.yyyy")
                            .apply { timeZone = TimeZone.getTimeZone("UTC") }
                            .format(calendar.time)
                    )
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.maxDate = maxDate
                show()
            }
        }

        timeEditText.isFocusable = false
        timeEditText.setOnClickListener {
            TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    calendar.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        hourOfDay,
                        minute,
                        0
                    )
                    calendar.time
                    timeEditText.setText(
                        SimpleDateFormat("hh:mm")
                            .apply { timeZone = TimeZone.getTimeZone("UTC") }
                            .format(calendar.time)
                    )
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        val parameterType = intent.getSerializableExtra(PARAMETER_TYPE_PARAM) as BodyParameterType

        secondValueLayout.hide()
        secondValueDelimiter.hide()

        when (parameterType) {
            is BodyParameterType.Height -> {
                nameEditText.setText(getString(R.string.height))
                unitEditText.setText(getText(R.string.cm))
                nameEditText.isFocusable = false
                unitEditText.isFocusable = false
                saveButton.setOnClickListener {
                    val centimeters = firstValueEditText.positiveDoubleOrNull()?.takeIf { it > 0 }
                    if (centimeters == null) {
                        toast(R.string.parameter_save_error)
                    } else {
                        finishWithBodyParameter(HeightModel(randomUUID().toString(), timestamp, centimeters))
                    }
                }
            }
            is BodyParameterType.Weight -> {
                nameEditText.setText(getString(R.string.weight))
                unitEditText.setText(getText(R.string.kg))
                nameEditText.isFocusable = false
                unitEditText.isFocusable = false
                saveButton.setOnClickListener {
                    val kilograms = firstValueEditText.positiveDoubleOrNull()?.takeIf { it > 0 }
                    if (kilograms == null) {
                        toast(R.string.parameter_save_error)
                    } else {
                        finishWithBodyParameter(WeightModel(randomUUID().toString(), timestamp, kilograms))
                    }
                }
            }
            is BodyParameterType.BloodOxygen -> {
                nameEditText.setText(getString(R.string.blood_oxygen))
                unitEditText.setText(getText(R.string.percent))
                nameEditText.isFocusable = false
                unitEditText.isFocusable = false
                saveButton.setOnClickListener {
                    val percents = firstValueEditText.positiveIntOrNull()?.takeIf { it <= 100 }
                    if (percents == null) {
                        toast(R.string.parameter_save_error)
                    } else {
                        finishWithBodyParameter(BloodOxygenModel(randomUUID().toString(), timestamp, percents))
                    }
                }
            }
            is BodyParameterType.BloodPressure -> {
                nameEditText.setText(getString(R.string.blood_pressure))
                unitEditText.setText(getText(R.string.mmHg))
                nameEditText.isFocusable = false
                unitEditText.isFocusable = false
                firstValueLayout.hint = getString(R.string.systolic_value)
                secondValueLayout.hint = getString(R.string.diastolic_value)
                secondValueLayout.show()
                secondValueDelimiter.show()

                saveButton.setOnClickListener {
                    val first = firstValueEditText.positiveIntOrNull()
                    val second = secondValueEditText.positiveIntOrNull()
                    if (first == null || second == null) {
                        toast(R.string.parameter_save_error)
                    } else {
                        finishWithBodyParameter(BloodPressureModel(randomUUID().toString(), timestamp, first, second))
                    }
                }
            }
            is BodyParameterType.BloodSugar -> {
                nameEditText.setText(getString(R.string.blood_sugar))
                unitEditText.setText(getText(R.string.mmol_per_liter))
                nameEditText.isFocusable = false
                unitEditText.isFocusable = false
                saveButton.setOnClickListener {
                    val mmolPerLiter = firstValueEditText.positiveDoubleOrNull()
                    if (mmolPerLiter == null) {
                        toast(R.string.parameter_save_error)
                    } else {
                        finishWithBodyParameter(BloodSugarModel(randomUUID().toString(), timestamp, mmolPerLiter))
                    }
                }
            }
            is BodyParameterType.Temperature -> {
                nameEditText.setText(getString(R.string.temperature))
                unitEditText.setText(getText(R.string.celcius))
                nameEditText.isFocusable = false
                unitEditText.isFocusable = false
                saveButton.setOnClickListener {
                    val celsius = firstValueEditText.positiveDoubleOrNull()
                    if (celsius == null) {
                        toast(R.string.parameter_save_error)
                    } else {
                        finishWithBodyParameter(TemperatureModel(randomUUID().toString(), timestamp, celsius))
                    }
                }
            }
            is BodyParameterType.Custom -> {
                if (parameterType != NEW) {
                    nameEditText.setText(parameterType.name)
                    unitEditText.setText(parameterType.unit)
                    nameEditText.isFocusable = false
                    unitEditText.isFocusable = false
                } else {
                    nameEditText.isFocusable = true
                    unitEditText.isFocusable = true
                }

                secondValueLayout.hide()
                secondValueDelimiter.hide()

                saveButton.setOnClickListener {
                    val value = firstValueEditText?.text?.toString()?.toDoubleOrNull()
                    val name = nameEditText?.text?.toString()?.takeIfNotBlank()
                    val unit = unitEditText?.text?.toString()?.takeIfNotBlank()
                    if (value == null || name == null || unit == null) {
                        toast(R.string.parameter_save_error)
                    } else {
                        finishWithBodyParameter(
                            CustomBodyParameterModel(
                                randomUUID().toString(),
                                timestamp,
                                name,
                                unit,
                                value
                            )
                        )
                    }
                }
            }
        }

    }

    private fun EditText.positiveIntOrNull(): Int? {
        return text?.toString()?.toIntOrNull()?.takeIf { it >= 0 }
    }

    private fun EditText.positiveDoubleOrNull(): Double? {
        return text?.toString()?.toDoubleOrNull()?.takeIf { it >= 0 }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun finishWithBodyParameter(bodyParameterModel: BodyParameterModel) {
        setResult(Activity.RESULT_OK, Intent().putExtra(PARAMETER_PARAM, bodyParameterModel))
        finish()
    }

    class IntentBuilder(fragment: Fragment) : CheckedIntentBuilder(fragment) {

        private var parameterType: BodyParameterType? = null

        fun parameterType(parameterType: BodyParameterType) = apply { this.parameterType = parameterType }

        override fun areParamsValid() = parameterType != null

        override fun get(): Intent = Intent(context, AddParameterActivity::class.java)
            .putExtra(PARAMETER_TYPE_PARAM, parameterType)

    }

}