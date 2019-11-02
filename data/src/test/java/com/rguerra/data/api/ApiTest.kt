package com.rguerra.data.api

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.rguerra.data.network.api.Api
import com.rguerra.data.network.entities.user.CompanyEntity
import com.rguerra.data.network.entities.user.UserEntity
import com.rguerra.data.network.service.Service
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ApiTest {

    @InjectMocks
    private lateinit var api: Api

    @Mock
    private lateinit var service: Service

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    @Throws(Exception::class)
    fun getPublicRepos() {
        //given
        whenever(service.getUsers())
                .thenReturn(Single.just(listOf(UserEntity(1, "androidRepo", CompanyEntity("https://github.com/github")))))
        //when
        api.getUsers()
        //then
        verify(service).getUsers()
        verifyNoMoreInteractions(service)
    }
}