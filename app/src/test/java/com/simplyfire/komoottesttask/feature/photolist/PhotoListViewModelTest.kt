package com.simplyfire.komoottesttask.feature.photolist

import com.simplyfire.komoottesttask.core.data.PhotoRepository
import com.simplyfire.komoottesttask.core.utils.Event
import com.simplyfire.komoottesttask.testutils.BaseViewModelTest
import com.simplyfire.komoottesttask.testutils.TestSchedulerProvider
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class PhotoListViewModelTest: BaseViewModelTest() {

    private val photoRepository = mock(PhotoRepository::class.java)

    @Test
    fun `Request location permissions when not granted`() {
        //arrange
        `when`(photoRepository.getPhotos()).thenReturn(Observable.empty())
        val viewModel = PhotoListViewModel(photoRepository, TestSchedulerProvider())

        //act
        viewModel.onStartClicked(false)

        //assert
        val expected = PhotoListViewModel.ViewState(requestLocationPermission = Event(true))
        assertEquals(expected, viewModel.viewState.value)
    }


}