package com.simplyfire.komoottesttask.feature.photolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.simplyfire.komoottesttask.core.data.PhotoRepository
import com.simplyfire.komoottesttask.core.utils.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

private const val TAG = "PhotoListViewModel"

class PhotoListViewModel @Inject constructor(photoRepository: PhotoRepository) : ViewModel() {
    val viewState: LiveData<ViewState> get() = _viewState
    private var _viewState = MediatorLiveData<ViewState>()
            // default value
        .apply { value = ViewState() }
    private val disposables = CompositeDisposable()

    init {
        disposables += photoRepository.getPhotos()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ photos ->
                val displayablePhotoList = photos.map { DisplayablePhoto(it.url_c) }
                updateState {copy(photos = displayablePhotoList)}
            }, { error ->
                throw RuntimeException(error)
            })
    }

    fun onLocationPermissionGranted() {
        updateState{copy(startLocationTracker = Event(true))}
    }

    fun onLocationPermissionDenied() {
        updateState{copy( error = Event(Error.LOCATION_PERMISSION_DENIED))}
    }

    fun onStopClicked() {
        updateState{copy( stopLocationTracker = Event(true), isLocationTrackingActive = false)}
    }

    fun onStartClicked(isLocationPermissionGranted: Boolean) {
        if(isLocationPermissionGranted) {
            updateState{copy(startLocationTracker = Event(true), isLocationTrackingActive = true)}
        } else {
            updateState{copy(requestLocationPermission = Event(true))}
        }
    }

    fun onLocationDisabled() {
        updateState{copy(error = Event(Error.LOCATION_DISABLED))}
    }

    private fun updateState(block: ViewState.() -> ViewState) {
        _viewState.value = getCurrViewState().block()
    }

    private fun getCurrViewState() = _viewState.value!!

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    data class ViewState(
        val photos: List<DisplayablePhoto> = listOf(),
        val isLocationTrackingActive: Boolean = false,
        val startLocationTracker: Event<Boolean> = Event(false),
        val stopLocationTracker: Event<Boolean> = Event(false),
        val requestLocationPermission: Event<Boolean> = Event(false),
        val error: Event<Error?> = Event(null)
    )

    enum class Error {
        LOCATION_DISABLED,
        LOCATION_PERMISSION_DENIED
    }
}