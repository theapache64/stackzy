package com.theapache64.stackzy.data.repo

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.github.theapache64.gpa.api.Play
import com.theapache64.expekt.should
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.runBlockingUnitTest
import com.toxicbakery.logging.Arbor
import it.cosenonjaviste.daggermock.InjectFromComponent
import kotlinx.coroutines.delay
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

    companion object {
        private lateinit var api: GooglePlayAPI
    }

    @BeforeAll
    @Test
    fun beforeAll() = runBlockingUnitTest {
        val username = System.getenv("PLAY_API_GOOGLE_USERNAME")!!
        val password = System.getenv("PLAY_API_GOOGLE_PASSWORD")!!

        val account = Play.login(username, password)
        Arbor.d("Logging in...")
        delay(3000) // delay to sync account
        api = Play.getApi(account)
        Arbor.d("Setting API : $api")
    }

    @Test
    fun givenKeyword_whenSearch_thenSuccess() = runBlockingUnitTest {
        val maxSearchResult = 30
        playStoreRepo.search(
            "WhatsApp",
            api,
            maxSearchResult
        ).size.should.above(maxSearchResult - 1) // more than or equal
    }
}