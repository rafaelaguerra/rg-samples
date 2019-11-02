package com.rguerra.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rguerra.domain.models.ProfileModel
import com.rguerra.domain.usecases.BaseUseCase
import com.rguerra.domain.usecases.GetProfilesCase
import com.rguerra.presentation.Data
import com.rguerra.presentation.Failure
import com.rguerra.presentation.models.ProfileUi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class ProfileListViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @InjectMocks
    private lateinit var viewModel: ProfileListViewModel

    @Mock
    private lateinit var useCaseUsers: GetProfilesCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    @Throws(Exception::class)
    fun loadMoreData_onError() {
        //given
        val profileUi = ProfileUi(
                id = 111,
                name = "name",
                numberPosts = "12",
                numberAlbums = "1",
                company = "company"
        )

        whenever(useCaseUsers.execute(any(), any(), any()))
                .thenAnswer { invocation ->
                    (invocation.arguments[1] as (Throwable) -> Unit).invoke(Throwable("error"))
                }
        //when
        viewModel.loadData(profileUi)
        //then
        assertTrue(viewModel.data.value is Failure)
        assertEquals((viewModel.data.value as Failure).errorMsg, "error")
        assertEquals(viewModel.loading.value, false)
    }


    @Test
    @Throws(Exception::class)
    fun loadMoreData_onSuccess() {
        //given
        val paramCaptor = argumentCaptor<BaseUseCase.EmptyParams>()

        val expectedUiList = listOf(
                ProfileUi(
                        id = 111,
                        name = "name",
                        numberPosts = "12",
                        numberAlbums = "1",
                        company = "company"
                )
        )

        whenever(useCaseUsers.execute(any(), any(), any()))
                .thenAnswer { invocation ->
                    (invocation.arguments[0] as (List<ProfileModel>) -> Unit).invoke(listOf(
                            com.rguerra.domain.models.ProfileModel(
                                    id = 111,
                                    numberOfAlbums = 1,
                                    numberOfPosts = 12,
                                    name = "name",
                                    company = "company"

                            )
                    ))
                }
        //when
        viewModel.loadData(null)
        //then
        verify(useCaseUsers).execute(any(), any(), paramCaptor.capture())
        assertTrue(viewModel.data.value is Data)
        assertEquals((viewModel.data.value as Data).data, expectedUiList)
        assertEquals(viewModel.loading.value, false)
    }

    @Test
    @Throws(Exception::class)
    fun saveSelectedProfile() {
        //given
        val selectedProfile = ProfileUi(
                id = 111,
                name = "name",
                numberPosts = "12",
                numberAlbums = "1",
                company = "company"
        )

        val expectedUiList = listOf(
                ProfileUi(
                        id = 111,
                        name = "name",
                        numberPosts = "12",
                        numberAlbums = "1",
                        company = "company",
                        selected = true
                ),
                ProfileUi(
                        id = 112,
                        name = "name112",
                        numberPosts = "55",
                        numberAlbums = "12",
                        company = "company112",
                        selected = false
                )
        )

        whenever(useCaseUsers.execute(any(), any(), any()))
                .thenAnswer { invocation ->
                    (invocation.arguments[0] as (List<com.rguerra.domain.models.ProfileModel>) -> Unit).invoke(listOf(
                            ProfileModel(
                                    id = 111,
                                    numberOfAlbums = 1,
                                    numberOfPosts = 12,
                                    name = "name",
                                    company = "company"

                            ),
                            ProfileModel(
                                    id = 112,
                                    numberOfAlbums = 12,
                                    numberOfPosts = 55,
                                    name = "name112",
                                    company = "company112"

                            )
                    ))
                }
        viewModel.loadData(selectedProfile)
        //when
        viewModel.saveSelectedProfile(selectedProfile)
        //then
        assertTrue(viewModel.data.value is Data)
        assertEquals((viewModel.data.value as Data).data, expectedUiList)
        assertEquals(viewModel.loading.value, false)
    }


    @Test
    @Throws(Exception::class)
    fun onStop() {
        //given
        //when
        viewModel.onStop()
        //then
        verify(useCaseUsers).clean()
    }
}