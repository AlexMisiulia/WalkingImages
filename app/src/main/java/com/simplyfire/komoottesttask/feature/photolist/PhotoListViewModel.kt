package com.simplyfire.komoottesttask.feature.photolist

import androidx.lifecycle.*
import com.simplyfire.komoottesttask.core.data.PhotoRepository
import javax.inject.Inject

class PhotoListViewModel @Inject constructor(photoRepository: PhotoRepository) : ViewModel() {
    val viewState: LiveData<ViewState> get() = _viewState
    private var _viewState = MediatorLiveData<ViewState>()

    init {
        _viewState.addSource(photoRepository.getPhotos()) { photos ->
            val displayablePhotoList = photos.map { DisplayablePhoto(it.url_c) }
            _viewState.value = ViewState(displayablePhotoList)
        }
    }

    data class ViewState(val photos: List<DisplayablePhoto>)
}