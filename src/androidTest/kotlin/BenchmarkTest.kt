package dev.gobley.test.jnadirectmapping.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BenchmarkTest {
    @Test
    fun runBenchmark() {
        Benchmark.run(30, 100_000)
    }
}