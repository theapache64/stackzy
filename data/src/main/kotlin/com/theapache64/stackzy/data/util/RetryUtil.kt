package com.theapache64.stackzy.data.util

import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime


/*
Generic class which hold Success and Error Response
*/
sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val exception: Exception) : Result<T>()
}

private const val DEFAULT_RETRY_LIMIT = 2
private const val DEFAULT_BACKOFF_MULTIPLIER = 2
private const val DEFAULT_BACKOFF_BASE = 2
private const val DEFAULT_IS_LOG_ENABLED = false


/**
 * Generic method to fire given [block] with exponential backOff based on given condition ([retryIf]).
 *
 * Exception from [block]:
 *  if during retry, it'll be passed through [retryIf].
 *  if in the last retry, propagated to call site.
 */
@OptIn(ExperimentalTime::class)
suspend fun <T> withExponentialBackOff(
    retryLimit: Int = DEFAULT_RETRY_LIMIT,
    backoffMultiplier: Int = DEFAULT_BACKOFF_MULTIPLIER,
    backOffBase: Int = DEFAULT_BACKOFF_BASE,
    isLogEnabled: Boolean = DEFAULT_IS_LOG_ENABLED,
    retryIf: (retryResult: Result<T>, retryCount: Int) -> Boolean,
    block: suspend (retryCount: Int, previousException: Exception?) -> T
): T {
    var retryCount = 0
    var result: Result<T>
    var previousException: Exception? = null
    do {
        if (retryCount != 0) {
            // API failed. backoff needed
            val temp = (backOffBase * backoffMultiplier.toDouble().pow(retryCount)) / 2
            val jitter = (0..temp.roundToInt()).random() // randomness to avoid parallel retry
            val sleep = (temp + jitter).toLong().seconds
            if (isLogEnabled) {
                println("Sleeping for $sleep (temp: $temp + jitter: $jitter) -  Retry: $retryCount/$retryLimit")
            }
            delay(sleep)
        }
        // If there's any exception happened while executing the block, we'll pass it to retryIf block
        // to decide if we should retry
        result = try {
            Result.Success(block(retryCount, previousException))
        } catch (e: Exception) {
            previousException = e
            Result.Error(e)
        }
        retryCount++
    } while (retryIf(result, retryCount))

    // final result
    return when (result) {
        is Result.Error -> throw result.exception
        is Result.Success -> result.data
    }
}
