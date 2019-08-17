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
    fun `Request location permissions when start button clicked and permission not granted`() {
        //arrange
        `when`(photoRepository.getPhotos()).thenReturn(Observable.empty())
        val viewModel = PhotoListViewModel(photoRepository, TestSchedulerProvider())

        //act
        viewModel.onStartClicked(false)

        //assert
        val expected = PhotoListViewModel.ViewState(requestLocationPermission = Event(true))
        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `Start location tracking when start button clicked and permission granted`() {
        //arrange
        `when`(photoRepository.getPhotos()).thenReturn(Observable.empty())
        val viewModel = PhotoListViewModel(photoRepository, TestSchedulerProvider())

        //act
        viewModel.onStartClicked(true)

        //assert
        val expected = PhotoListViewModel.ViewState(startLocationTracker = Event(true), isLocationTrackingActive = true)
        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `Start location tracking when permission granted`() {
        //arrange
        `when`(photoRepository.getPhotos()).thenReturn(Observable.empty())
        val viewModel = PhotoListViewModel(photoRepository, TestSchedulerProvider())

        //act
        viewModel.onLocationPermissionGranted()

        //assert
        val expected = PhotoListViewModel.ViewState(startLocationTracker = Event(true), isLocationTrackingActive = true)
        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `Stop location tracking when stop clicked`() {
        //arrange
        `when`(photoRepository.getPhotos()).thenReturn(Observable.empty())
        val viewModel = PhotoListViewModel(photoRepository, TestSchedulerProvider())

        //act
        viewModel.onStopClicked()

        //assert
        val expected = PhotoListViewModel.ViewState(stopLocationTracker = Event(true), isLocationTrackingActive = false)
        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `Show location disabled error when location disabled`() {
        //arrange
        `when`(photoRepository.getPhotos()).thenReturn(Observable.empty())
        val viewModel = PhotoListViewModel(photoRepository, TestSchedulerProvider())

        //act
        viewModel.onLocationAvailable(false)

        //assert
        val expected = PhotoListViewModel.ViewState(error = Event(PhotoListViewModel.Error.LOCATION_DISABLED))
        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `Show location permission denied error when location permission not granted`() {
        //arrange
        `when`(photoRepository.getPhotos()).thenReturn(Observable.empty())
        val viewModel = PhotoListViewModel(photoRepository, TestSchedulerProvider())

        //act
        viewModel.onLocationPermissionDenied()

        //assert
        val expected = PhotoListViewModel.ViewState(error = Event(PhotoListViewModel.Error.LOCATION_PERMISSION_DENIED))
        assertEquals(expected, viewModel.viewState.value)
    }

}