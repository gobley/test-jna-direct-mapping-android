package dev.gobley.test.jnadirectmapping.android

import com.sun.jna.Native
import java.time.Duration
import java.time.Instant
import kotlin.math.sqrt

object Benchmark {
    fun run(numSamples: Int, numInvocations: Int) {
        println("=== Interface mapped ===")
        val interfaceMapped = Native.load("jna_direct_mapping_android", RustLibrary::class.java)
        runBenchmark(interfaceMapped, numSamples, numInvocations)
        println()

        println("=== Direct mapped ===")
        val directMapped = NativeMappedRustLibrary()
        runBenchmark(directMapped, numSamples, numInvocations)
        println()
    }

    private fun runBenchmark(rustLibrary: RustLibrary, numSamples: Int, numInvocations: Int) {
        val results = (0 until numSamples).map {
            runSingleBenchmark(rustLibrary, numInvocations)
        }
        val averageSeconds = results
            .map { duration -> duration.toNanos() / 1000000000.0 }
            .average()
        val varianceSeconds = results
            .map { duration ->
                val seconds = duration.toNanos() / 1000000000.0
                val difference = averageSeconds - seconds
                difference * difference
            }
            .average()
        val stdDevSeconds = sqrt(varianceSeconds)
        System.out.printf(
            "Average (n = %d): %f\nStddev (n = %d): %f\n",
            numSamples,
            averageSeconds,
            numSamples,
            stdDevSeconds
        )
    }

    private fun runSingleBenchmark(rustLibrary: RustLibrary, numInvocations: Int): Duration {
        val startTime = Instant.now()
        for (i in 0 until numInvocations) {
            rustLibrary.add(3, 4)
        }
        val endTime = Instant.now()
        return Duration.between(startTime, endTime)
    }

    private interface RustLibrary : com.sun.jna.Library {
        fun add(lhs: Int, rhs: Int): Int
    }

    private class NativeMappedRustLibrary : RustLibrary {
        external override fun add(lhs: Int, rhs: Int): Int

        companion object {
            init {
                Native.register(NativeMappedRustLibrary::class.java, "jna_direct_mapping_android")
            }
        }
    }
}