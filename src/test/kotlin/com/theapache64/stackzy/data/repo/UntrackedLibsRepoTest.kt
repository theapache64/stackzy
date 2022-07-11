package com.theapache64.stackzy.data.repo

import com.github.theapache64.expekt.should
import com.theapache64.stackzy.data.remote.UntrackedLibrary
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.runBlockingUnitTest
import it.cosenonjaviste.daggermock.InjectFromComponent
import org.junit.Rule
import org.junit.Test

internal class UntrackedLibsRepoTest {

    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var untrackedLibsRepo: UntrackedLibsRepo

    @Test
    fun `Add new untracked library`() = runBlockingUnitTest {
        val inputPackageName = "com.test.package"
        untrackedLibsRepo
            .add(UntrackedLibrary(inputPackageName))
            .collect {
                when (it) {
                    is Resource.Loading -> {
                        // do nothing
                    }
                    is Resource.Success -> {
                        it.data.packageNames.should.equal(inputPackageName)
                    }
                    is Resource.Error -> {
                        assert(false)
                    }
                }
            }
    }

    @Test
    fun `Get untracked packages`() = runBlockingUnitTest {
        untrackedLibsRepo
            .getUntrackedLibs()
            .collect {
                when (it) {
                    is Resource.Loading -> {
                        // do nothing
                    }
                    is Resource.Success -> {
                        it.data.size.should.above(0)
                    }
                    is Resource.Error -> {
                        assert(false)
                    }
                }
            }
    }

}