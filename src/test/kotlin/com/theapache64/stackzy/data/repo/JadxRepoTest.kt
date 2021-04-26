package com.theapache64.stackzy.data.repo

import com.theapache64.expekt.should
import com.theapache64.stackzy.test.MyDaggerMockRule
import it.cosenonjaviste.daggermock.InjectFromComponent
import org.junit.Rule
import org.junit.Test
import kotlin.io.path.exists

internal class JadxRepoTest {
    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var jadxRepo: JadxRepo

    @Test
    fun jadxDirExist() {
        jadxRepo.jadxDirPath.exists().should.`true`
    }
}