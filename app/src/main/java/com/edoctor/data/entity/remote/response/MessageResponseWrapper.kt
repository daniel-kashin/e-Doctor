package com.edoctor.data.entity.remote.response

data class MessageResponseWrapper(
    val textMessageResponse: TextMessageResponse?,
    val callStatusMessageResponse: CallStatusMessageResponse?
)