package com.theapache64.stackzy.data.repo

import com.theapache64.expekt.should
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.runBlockingUnitTest
import com.theapache64.stackzy.utils.calladapter.flow.Resource
import it.cosenonjaviste.daggermock.InjectFromComponent
import kotlinx.coroutines.flow.collect
import org.junit.Rule
import org.junit.Test

internal class CategoriesRepoTest {

    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var categoriesRepo: CategoriesRepo

    @Test
    fun `Categories have data`() = runBlockingUnitTest {
        categoriesRepo.getRemoteCategories()
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