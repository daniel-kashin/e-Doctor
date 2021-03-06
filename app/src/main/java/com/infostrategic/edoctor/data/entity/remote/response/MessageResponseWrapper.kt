package com.infostrategic.edoctor.data.entity.remote.response

data class MessageResponseWrapper(
    val textMessageResponse: TextMessageResponse?,
    val callStatusMessageResponse: CallStatusMessageResponse?,
    val medicalAccessesMessageResponse: MedicalAccessesMessageResponse?,
    val medicalRecordRequestResponse: MedicalRecordRequestMessageResponse?,
    val imageMessageResponse: ImageMessageResponse?
)