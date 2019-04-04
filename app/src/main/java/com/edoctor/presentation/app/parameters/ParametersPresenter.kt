package com.edoctor.presentation.app.parameters

import com.edoctor.data.entity.remote.model.record.BodyParameterModel
import com.edoctor.data.injection.ApplicationModule
import com.edoctor.data.repository.MedicalRecordsRepository
import com.edoctor.presentation.app.parameters.ParametersPresenter.Event
import com.edoctor.presentation.app.parameters.ParametersPresenter.ViewState
import com.edoctor.presentation.architecture.presenter.Presenter
import com.edoctor.utils.nothing
import com.edoctor.utils.plusAssign
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class ParametersPresenter @Inject constructor(
    val medicalRecordsRepository: MedicalRecordsRepository,
    @Named(ApplicationModule.MAIN_THREAD_SCHEDULER)
    private val observeScheduler: Scheduler,
    @Named(ApplicationModule.IO_THREAD_SCHEDULER)
    private val subscribeScheduler: Scheduler
) : Presenter<ViewState, Event>() {

    init {
        setViewState(ViewState(emptyList()))

        disposables += medicalRecordsRepository.getLatestBodyParametersOfEachType()
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribe({
                setViewState { copy(bodyParameters = it) }
            }, { throwable ->
                nothing()
            })
    }

    data class ViewState(
        val bodyParameters: List<BodyParameterModel>
    ) : Presenter.ViewState

    class Event : Presenter.Event

}