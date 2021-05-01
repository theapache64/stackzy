package com.theapache64.stackzy.data.repo

import com.github.theapache64.gpa.model.Account
import com.github.theapache64.expekt.should
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.runBlockingUnitTest
import com.toxicbakery.logging.Arbor
import it.cosenonjaviste.daggermock.InjectFromComponent
import kotlinx.coroutines.flow.collect
import org.junit.Rule
import org.junit.Test

class AuthRepoTest {
    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var authRepo: AuthRepo

    @Test
    fun givenValidCreds_whenLogIn_thenSuccess() = runBlockingUnitTest {
        val username = System.getenv("PLAY_API_GOOGLE_USERNAME")!!
        val password = System.getenv("PLAY_API_GOOGLE_PASSWORD")!!

        authRepo.logIn(username, password).collect {
            when (it) {
                is Resource.Loading -> {
                    Arbor.d("logging in...")
                }
                is Resource.Success -> {
                    it.data.username.should.equal(username)
                }
                is Resource.Error -> {
                    assert(false)
                }
            }
        }
    }

    @Test
    fun givenInvalidCreds_whenLogIn_thenError() = runBlockingUnitTest {

        authRepo.logIn("", "").collect {
            when (it) {
                is Resource.Loading -> {
                    Arbor.d("logging in...")
                }
                is Resource.Success -> {
                    assert(false)
                }
                is Resource.Error -> {
                    assert(true)
                }
            }
        }
    }

    @Test
    fun givenAccount_whenStoredGetAndCleared_thenSuccess() {
        val dummyAccount = Account(
            username = "john.doe",
            password = "pass1234",
            token = "someSecureToken",
            gsfId = "jhj45k34h5k3h45kjh34k",
            locale = "en-IN"
        )
        authRepo.storeAccount(dummyAccount)
        authRepo.getAccount().should.equal(dummyAccount)

        authRepo.logout() // test finished, so delete account
        authRepo.getAccount().should.`null`
    }
}