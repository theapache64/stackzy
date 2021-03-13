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

    companion object {
        private const val TEST_PACKAGE_NAME = "com.theapache64.test.app"
    }

    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var resultRepo: ResultRepo

    @Test
    fun `Add result`() = runBlockingUnitTest {
        val result = Result(
            appName = "Test App",
            packageName = TEST_PACKAGE_NAME,
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

    @Test
    fun `Find result with valid package name`() = runBlockingUnitTest {
        resultRepo
            .findResult(TEST_PACKAGE_NAME, 123456)
            .collect {
                when (it) {
                    is Resource.Loading -> {
                        println("Finding...")
                    }

                    is Resource.Success -> {
                        it.data.packageName.should.equal(TEST_PACKAGE_NAME)
                    }

                    is Resource.Error -> {
                        assert(false) {
                            it.errorData
                        }
                    }
                }
            }
    }

    @Test
    fun `Find result with invalid package name`() = runBlockingUnitTest {
        resultRepo
            .findResult("com.theapache64.this.app.does.not.exist", 111111)
            .collect {
                when (it) {
                    is Resource.Loading -> {
                        println("Finding...")
                    }

                    is Resource.Success -> {
                        assert(false) {
                            "This app shouldn't exist but returned $it"
                        }
                    }

                    is Resource.Error -> {
                        assert(true)
                    }
                }
            }
    }
}