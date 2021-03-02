package com.theapache64.stackzy.util

import com.theapache64.expekt.should
import org.junit.Test

class AndroidVersionIdentifierTest {
    @Test
    fun `Single version`() {
        AndroidVersionIdentifier.getVersion(3).should.equal("Cupcake")
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
