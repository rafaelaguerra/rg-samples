package com.rguerra.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.rguerra.domain.usecases.SeedInitialDataCase
import com.rguerra.presentation.Data
import com.rguerra.presentation.Failure
import com.rguerra.presentation.NetworkAndroidResourceUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SplashViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @InjectMocks
    private lateinit var viewModel: SplashViewModel

    @Mock
    private lateinit var networkUtil: NetworkAndroidResourceUtil

    @Mock
    private lateinit var seedInitialDataCase: SeedInitialDataCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onStart_no_network_connection() {
        //given
        whenever(networkUtil.isNetworkConnected()).thenReturn(false)
        //when
        viewModel.onStart()
        //then
        assertTrue(viewModel.networkConnection.value is Data)
        assertEquals((viewModel.networkConnection.value as Data).data, true)
        verifyZeroInteractions(seedInitialDataCase)
    }

    @Test
    fun onStart_with_network_connection() {
        //given
        whenever(networkUtil.isNetworkConnected()).thenReturn(true)
        //when
        viewModel.onStart()
        //then
        assertTrue(viewModel.networkConnection.value is Data)
        assertEquals((viewModel.networkConnection.value as Data).data, false)
        verify(seedInitialDataCase).execute(any(), any(), any())
    }

    @Test
    fun onStart_getData_success() {
        //given
        whenever(networkUtil.isNetworkConnected()).thenReturn(true)
        //when
        viewModel.onStart()
        //then
        verify(seedInitialDataCase).execute(any(), any(), any())
        assertEquals((viewModel.networkConnection.value as Data).data, false)
    }

    @Test
    fun onStart_getData_error() {
        //given
        whenever(seedInitialDataCase.execute(any(), any(), any()))
                .thenAnswer { invocation ->
                    (invocation.arguments[1] as (Throwable) -> Unit).invoke(Throwable("error"))
                }
        whenever(networkUtil.isNetworkConnected()).thenReturn(true)
        //when
        viewModel.onStart()
        //then
        verify(seedInitialDataCase).execute(any(), any(), any())
        assertTrue(viewModel.result.value is Failure)
        assertEquals((viewModel.result.value as Failure).errorMsg, "error")
        assertEquals((viewModel.networkConnection.value as Data).data, false)
    }

    @Test
    fun onStop() {
        //given
        //when
        viewModel.onStop()
        //then
        verify(seedInitialDataCase).clean()
    }
}