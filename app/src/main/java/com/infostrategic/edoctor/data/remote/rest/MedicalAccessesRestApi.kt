package com.infostrategic.edoctor.data.remote.rest

import com.infostrategic.edoctor.data.entity.remote.model.medicalAccess.MedicalAccessesForDoctorModel
import com.infostrategic.edoctor.data.entity.remote.model.medicalAccess.MedicalAccessesForPatientModel
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MedicalAccessesRestApi {

    @GET("/medicalAccessesForDoctor")
    fun getMedicalAccessesForDoctor(
        @Query("patientUuid") patientUuid: String?
    ) : Single<MedicalAccessesForDoctorModel>

    @GET("/medicalAccessesForPatient")
    fun getMedicalAccessesForPatient(
        @Query("doctorUuid") doctorUuid: String?
    ) : Single<MedicalAccessesForPatientModel>

    @POST("/medicalAccessesForPatient")
    fun postMedicalAccessesForPatient(
        @Body medicalAccesses: MedicalAccessesForPatientModel
    ) : Completable

}