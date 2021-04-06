package com.theapache64.stackzy.data.repo

import com.theapache64.expekt.should
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.runBlockingUnitTest
import it.cosenonjaviste.daggermock.InjectFromComponent
import kotlinx.coroutines.flow.collect
import org.junit.Rule
import org.junit.Test

internal class LibrariesRepoTest {

    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var librariesRepo: LibrariesRepo

    @Test
    fun `Libraries have data`() = runBlockingUnitTest {
        librariesRepo.getRemoteLibraries()
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