package com.theapache64.stackzy.data.repo

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.github.theapache64.expekt.should
import com.github.theapache64.gpa.api.Play
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.runBlockingUnitTest
import com.toxicbakery.logging.Arbor
import it.cosenonjaviste.daggermock.InjectFromComponent
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayStoreRepoTest {

    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var playStoreRepo: PlayStoreRepo

    @InjectFromComponent
    private lateinit var authRepo: AuthRepo

    companion object {
        private lateinit var api: GooglePlayAPI
    }

    @BeforeAll
    @Test
    fun beforeAll() = runBlockingUnitTest {
        val username = System.getenv("PLAY_API_GOOGLE_USERNAME")!!
        val password = System.getenv("PLAY_API_GOOGLE_PASSWORD")!!

        authRepo.logIn(username, password).collect {
            when (it) {
                is Resource.Loading -> {
                    Arbor.d("logging in...")
                }
                is Resource.Success -> {
                    api = Play.getApi(account = it.data)
                }
                is Resource.Error -> {
                    assert(false)
                }
            }
        }
    }

    @Test
    fun givenKeyword_whenSearch_thenSuccess() = runBlockingUnitTest {
        val maxSearchResult = 10
        playStoreRepo.search(
            "WhatsApp",
            api,
            maxSearchResult
        ).size.should.above(maxSearchResult - 1) // more than or equal
    }

    @Test
    fun givenPackageName_whenFind_thenReturnValidDetails() = runBlockingUnitTest {
        playStoreRepo.find(
            "com.theapache64.papercop",
            api
        )?.appTitle.should.equal("Paper Cop")
    }

    @Test
    fun givenInvalidPackageName_whenFind_thenReturnNull() = runBlockingUnitTest {
        playStoreRepo.find(
            "com.theapache64.some.invalid.package.name",
            api
        ).should.`null`
    }
}