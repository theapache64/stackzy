package com.theapache64.stackzy.data.repo

import com.theapache64.expekt.should
import com.theapache64.stackzy.data.remote.Result
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.runBlockingUnitTest
import com.theapache64.stackzy.util.calladapter.flow.Resource
import it.cosenonjaviste.daggermock.InjectFromComponent
import kotlinx.coroutines.flow.collect
import org.junit.Rule
import org.junit.Test

class ResultRepoTest {
    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var resultRepo: ResultRepo

    @Test
    fun `Add result`() = runBlockingUnitTest {
        val result = Result(
            appName = "Test App",
            packageName = "com.theapache64.test.app",
            libPackages = "okhttp3, retrofit2",
            versionCode = 1234,
            versionName = "v1.2.3-alpha04"
        )

        resultRepo.add(result).collect {
            when (it) {
                is Resource.Loading -> {
                    println("Adding...")
                }
                is Resource.Success -> {
                    it.data.should.equal(result)
                }
                is Resource.Error -> {
                    assert(false) {
                        it.errorData
                    }
                }
            }
        }
    }
}