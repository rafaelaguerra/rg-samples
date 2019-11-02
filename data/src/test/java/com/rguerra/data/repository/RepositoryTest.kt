package com.rguerra.data.repository

import com.nhaarman.mockitokotlin2.*
import com.rguerra.data.network.api.Api
import com.rguerra.data.network.entities.album.AlbumEntity
import com.rguerra.data.network.entities.post.PostEntity
import com.rguerra.data.network.entities.user.CompanyEntity
import com.rguerra.data.network.entities.user.UserEntity
import com.rguerra.domain.models.Album
import com.rguerra.domain.models.Company
import com.rguerra.domain.models.Post
import com.rguerra.domain.models.User
import com.rguerra.domain.models.request.ByUserIdRequest
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class RepositoryTest {

    @InjectMocks
    private lateinit var repository: Repository

    @Mock
    private lateinit var api: Api

    @Spy
    private lateinit var storage: Storage

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        reset(api)
        reset(storage)
    }

    @Test
    @Throws(Exception::class)
    fun getUsers_validList() {
        //given
        val expectedList = listOf(User(1, "name", Company("http://github.com")))
        whenever(storage.users).thenReturn(null)
        whenever(api.getUsers())
                .thenReturn(Single.just(listOf(UserEntity(1, "name", CompanyEntity("http://github.com")))))
        //when
        val result = repository.getUsers()
                .test()
        //then
        verify(api).getUsers()
        verifyNoMoreInteractions(api)
        result.assertValue(expectedList)
    }

    @Test
    @Throws(Exception::class)
    fun getUsers_emptyList() {
        //given
        val expectedList = emptyList<User>()
        whenever(storage.users).thenReturn(null)
        whenever(api.getUsers())
                .thenReturn(Single.just(emptyList()))
        //when
        val result = repository.getUsers()
                .test()
        //then
        verify(api).getUsers()
        verifyNoMoreInteractions(api)
        result.assertValue(expectedList)
    }

    @Test
    @Throws(Exception::class)
    fun getData_taking_from_storage() {
        //given
        val albumMap = mutableMapOf(Pair(1, listOf(Album(""))))
        val postList = mutableMapOf(Pair(1, listOf(Post(1, " ", ""))))

        whenever(storage.users)
                .thenReturn(listOf())

        whenever(storage.albums)
                .thenReturn(albumMap)

        whenever(storage.posts)
                .thenReturn(postList)

        val request = ByUserIdRequest(1)
        //when
        repository.getUsers()
        repository.getAlbums(request)
        repository.getPosts(request)
        //then
        verify(storage, times(2)).users
        verify(storage, times(2)).albums
        verify(storage, times(2)).posts
        verifyNoMoreInteractions(storage)
        verifyZeroInteractions(api)
    }


    @Test
    @Throws(Exception::class)
    fun seedUsers() {
        //given
        val userList = listOf(UserEntity(1, "name", CompanyEntity("http://github.com")))
        val albumList = listOf(AlbumEntity(1, "titleAlbum"), AlbumEntity(2, "titleAlbum"))
        val postList = listOf(PostEntity(1, "titlePost", "body"), PostEntity(2, "titlePost", "body"), PostEntity(3, "titlePost", "body"))
        val request = ByUserIdRequest(1)

        whenever(api.getUsers())
                .thenReturn(Single.just(userList))
        whenever(api.getAlbums(request))
                .thenReturn(Single.just(albumList))
        whenever(api.gePosts(request))
                .thenReturn(Single.just(postList))
        //when
        val result = repository.seedUsers().test()
        //then
        verify(api).getUsers()
        verify(api).getAlbums(eq(request))
        verify(api).gePosts(eq(request))
        verifyNoMoreInteractions(api)
        assertTrue(storage.users?.size == 1)
        assertTrue(storage.albums[1]?.size == 2)
        assertTrue(storage.posts[1]?.size == 3)
        result.assertComplete()
    }


    @Test
    @Throws(Exception::class)
    fun getAlbums() {
        //given
        whenever(api.getAlbums(any()))
                .thenReturn(Single.just(emptyList()))

        val request = ByUserIdRequest(userId = 1)
        //when
        val result = repository.getAlbums(request).test()
        //then
        verify(api).getAlbums(eq(request))
        verifyNoMoreInteractions(api)
        result.assertComplete()
    }
}