package com.edoctor.presentation.app.medcardForDoctor

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.edoctor.R
import com.edoctor.data.entity.remote.model.user.PatientModel
import com.edoctor.presentation.app.medcard.MedcardFragment
import com.edoctor.presentation.app.patient.PatientActivity
import com.edoctor.utils.CheckedIntentBuilder
import com.edoctor.utils.lazyFind

class MedcardForDoctorActivity : AppCompatActivity() {

    companion object {
        const val PATIENT_PARAM = "patient"
    }

    private val toolbar by lazyFind<Toolbar>(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medcard_for_doctor)

        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
            title = getString(R.string.patient).capitalize()
        }

        val patient = intent?.getSerializableExtra(PatientActivity.PATIENT_PARAM) as PatientModel

        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, MedcardFragment.newInstance(patient))
                .commit()
        }
    }

    class IntentBuilder(context: Context) : CheckedIntentBuilder(context) {

        private var patient: PatientModel? = null

        fun patient(patient: PatientModel) = apply { this.patient = patient }

        override fun areParamsValid() = patient != null

        override fun get(): Intent =
            Intent(context, MedcardForDoctorActivity::class.java)
                .putExtra(PATIENT_PARAM, patient)

    }

}