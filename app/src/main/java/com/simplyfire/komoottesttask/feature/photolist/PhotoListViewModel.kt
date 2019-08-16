package com.simplyfire.komoottesttask.feature.photolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.simplyfire.komoottesttask.core.data.PhotoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

private const val TAG = "PhotoListViewModel"

class PhotoListViewModel @Inject constructor(photoRepository: PhotoRepository) : ViewModel() {
    val viewState: LiveData<ViewState> get() = _viewState
    private var _viewState = MediatorLiveData<ViewState>()
    private val disposables = CompositeDisposable()

    init {
        disposables += photoRepository.getPhotos()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ photos ->
                val displayablePhotoList = photos.map { DisplayablePhoto(it.url_c) }
                _viewState.value = ViewState(displayablePhotoList)
            }, { error ->
                throw RuntimeException(error)
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    data class ViewState(val photos: List<DisplayablePhoto>)
}