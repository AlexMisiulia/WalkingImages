package com.simplyfire.komoottesttask.feature.photolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simplyfire.komoottesttask.core.data.PhotoRepository
import javax.inject.Inject

class PhotoListViewModel @Inject constructor(photoRepository: PhotoRepository) : ViewModel() {
    val viewState: LiveData<ViewState> get() = _viewState
    private var _viewState = MutableLiveData<ViewState>()

    init {

    }

    data class ViewState(val photos: List<DisplayablePhoto>)
}