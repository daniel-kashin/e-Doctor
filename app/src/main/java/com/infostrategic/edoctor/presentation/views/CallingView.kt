package com.infostrategic.edoctor.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.infostrategic.edoctor.R
import com.infostrategic.edoctor.utils.changesObservableDelegate
import com.infostrategic.edoctor.utils.lazyFind
import com.infostrategic.edoctor.utils.show

class CallingView(private val _context: Context, attrs: AttributeSet) : FrameLayout(_context, attrs) {

    private val nameTextView by lazyFind<TextView>(R.id.name_text_view)
    private val statusTextView by lazyFind<TextView>(R.id.status_text_view)
    private val enableAudioImageView by lazyFind<CircleImageView>(R.id.enable_audio_image_view)
    private val acceptCallImageView by lazyFind<CircleImageView>(R.id.accept_call_image_view)
    private val declineCallImageView by lazyFind<CircleImageView>(R.id.decline_call_image_view)
    private val enableVideoImageView by lazyFind<CircleImageView>(R.id.enable_video_image_view)

    var isAudioEnabled: Boolean by changesObservableDelegate(false) { _, newValue ->
        if (newValue) {
            enableAudioImageView.bind(
                R.color.circle_button_enabled,
                R.color.circle_button_image_enabled,
                R.drawable.ic_mic
            )
        } else {
            enableAudioImageView.bind(R.color.circle_button_disabled, android.R.color.white, R.drawable.ic_mic_off)
        }
    }

    var isVideoEnabled: Boolean by changesObservableDelegate(true) { _, newValue ->
        if (newValue) {
            enableVideoImageView.bind(
                R.color.circle_button_enabled,
                R.color.circle_button_image_enabled,
                R.drawable.ic_videocam
            )
        } else {
            enableVideoImageView.bind(R.color.circle_button_disabled, android.R.color.white, R.drawable.ic_videocam_off)
        }
    }

    var onCallAcceptedListener: (() -> Unit)? = null

    var onCallDeclinedListener: (() -> Unit)? = null

    init {
        View.inflate(_context, R.layout.view_calling, this)
        isAudioEnabled = true
        isVideoEnabled = false
        acceptCallImageView.run {
            bind(R.color.accept_call, android.R.color.white, R.drawable.ic_accept_call)
            setOnClickListener { onCallAcceptedListener?.invoke() }
        }
        declineCallImageView.run {
            bind(R.color.decline_call, android.R.color.white, R.drawable.ic_decline_call)
            setOnClickListener { onCallDeclinedListener?.invoke() }
        }
        enableAudioImageView.setOnClickListener {
            isAudioEnabled = !isAudioEnabled
        }
        enableVideoImageView.setOnClickListener {
            isVideoEnabled = !isVideoEnabled
        }
    }

    fun bind(name: String?, callingType: CallType) {
        isAudioEnabled = true
        isVideoEnabled = false

        nameTextView.text = name
        statusTextView.text = when (callingType) {
            CallType.INCOMING -> "${_context.getString(R.string.incoming_call).toUpperCase()}..."
            CallType.OUTCOMING -> "${_context.getString(R.string.outcoming_call).toUpperCase()}..."
        }
        acceptCallImageView.show(callingType == CallType.INCOMING)
    }

    enum class CallType {
        INCOMING,
        OUTCOMING
    }

}