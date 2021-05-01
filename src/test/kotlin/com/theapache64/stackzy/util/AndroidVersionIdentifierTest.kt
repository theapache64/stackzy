package com.theapache64.stackzy.util

import com.github.theapache64.expekt.should
import com.theapache64.stackzy.data.util.AndroidVersionIdentifier
import org.junit.Test

class AndroidVersionIdentifierTest {
    @Test
    fun `Single version`() {
        AndroidVersionIdentifier.getVersion(3).should.equal("Cupcake")
        AndroidVersionIdentifier.getVersion(50).should.`null`
    }

    @Test
    fun `Range version`() {
        AndroidVersionIdentifier.getVersion(4).should.equal("Donut")
        AndroidVersionIdentifier.getVersion(5).should.equal("Eclair")
        AndroidVersionIdentifier.getVersion(6).should.equal("Eclair")
        AndroidVersionIdentifier.getVersion(7).should.equal("Eclair")
        AndroidVersionIdentifier.getVersion(8).should.equal("Froyo")
    }
}
