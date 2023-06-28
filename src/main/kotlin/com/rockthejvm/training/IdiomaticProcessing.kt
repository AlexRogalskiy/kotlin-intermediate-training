package com.rockthejvm.training

import arrow.core.*
import java.util.Random

object IdiomaticProcessing {

    // nullables & Option
    // job board
    data class Job(val jobId: JobId, val company: Company, val role: Role, val salary: Salary)

    @JvmInline
    value class JobId(val value: Int)
    @JvmInline
    value class Company(val value: String)
    @JvmInline
    value class Role(val value: String)
    @JvmInline
    value class Salary(val value: Double)

    // "database"

    interface Jobs {
        fun findByIdNaive(id: JobId): Job
        fun findByIdNullable(id: JobId): Job?
        fun findByIdO(id: JobId): Option<Job>
    }

    class LiveJobs: Jobs {
        private val JOBS_DATABASE: Map<JobId, Job> = mapOf(
            JobId(1) to Job(
                JobId(1),
                Company("Google"),
                Role("App developer"),
                Salary(100.0)
            ),
            JobId(2) to Job(
                JobId(2),
                Company("Google"),
                Role("Backend developer"),
                Salary(99.0)
            ),
            JobId(3) to Job(
                JobId(3),
                Company("Google"),
                Role("Cloud developer"),
                Salary(77.0)
            ),
        )

        override fun findByIdNaive(id: JobId): Job {
            val superjob: Job? = JOBS_DATABASE[id]
            if (superjob != null) {
                // we know superjob is a Job
                return superjob
            }
            else throw NoSuchElementException("job id $id not found")
        }

        override fun findByIdNullable(id: JobId): Job? =
            JOBS_DATABASE[id]

        override fun findByIdO(id: JobId): Option<Job> {
            TODO("Not yet implemented")
        }
    }

    val maybeJob: Job? = null

    @JvmStatic
    fun main(args: Array<String>) {
        val jobs: Jobs = LiveJobs()
        println(jobs.findByIdNullable(JobId(44)))
    }
}

object OptionDemo {

    fun gimmeMeaningOfLife(): Int? {
        return if (kotlin.random.Random.nextBoolean()) 42
        else null
    }
    fun invokeService(mol: Int): Option<String> =
        if (mol % 2 == 0) Some("ok")
        else None

    val anOption: Option<Int> = Option.fromNullable(gimmeMeaningOfLife())
    // 2 subtypes - Some(value), None
    val aNonEmptyOption: Option<Int> = Some(42)
    val anEmptyOption: Option<Int> = None
    val anEmptyOption_v2: Option<Int> = Option.fromNullable(null)

    // checks
    val checkEmpty = anOption.isEmpty()
    val mol = anOption.getOrElse { 44 }

    // FP - map, flatMap, filter
    val aMappedOption: Option<String> = anOption.map { (1..it).map {"Kotlin"}.joinToString("") }
    //                                                       Int -> String
    // flatMap for chained computations
    val flatMappedOption = anOption.flatMap { invokeService(it) }
    //                                        Int -> Option<String>

    /**
     * TODO
     *  - list of optional strings, combine them all into a single string
     *  - if all optionals are empty, return empty string
     */
    val strings: List<Option<String>> = listOf(Some("Kotlin"), None, Some("functional"), None, Some("programming"))


    @JvmStatic
    fun main(args: Array<String>) {

    }
}