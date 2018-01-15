package fr.guddy.kandroidmodular

import fr.guddy.kandroidmodular.di.netModule
import org.junit.Test
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.KoinTest
import org.koin.test.dryRun

class DryRunTest : KoinTest {
    @Test
    fun dryRunTest() {
        startKoin(listOf(netModule))
        dryRun()
        closeKoin()
    }
}