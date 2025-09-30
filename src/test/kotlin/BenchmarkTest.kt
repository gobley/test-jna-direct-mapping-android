package dev.gobley.test.jnadirectmapping.android

import org.junit.Test

class BenchmarkTest {
    @Test
    fun runBenchmark() {
        Benchmark.run(30, 100_000)
    }
}