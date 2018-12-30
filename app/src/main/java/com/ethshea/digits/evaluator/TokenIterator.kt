package com.ethshea.digits.evaluator

import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.misc.Predicate
import java.util.*

/**
 * @author Ethan
 */
class TokenIterator(tokens: String, private val location: Interval) {
    var position: Int = 0
        private set
    var previousPosition: Int = 0
        private  set
    private var tokens = tokens.replace("""\s*""".toRegex(), "")
    private var speculations = Stack<Int>()

    val previousLocation
        get() = Interval(location.a + previousPosition, location.a + position - 1) // b is inclusively indexed

    fun hasNext() = position < tokens.length
    val remaining get() = tokens.length - position
    fun peek() = tokens[position]
    fun isNext(test: String) : Boolean {
        return remaining >= test.length
                && tokens.subSequence(position, position + test.length) == test
    }

    fun isNext(test: Char) : Boolean {
        return hasNext() && tokens[position] == test
    }

    fun next() : Char {
        previousPosition = position
        val result = tokens[position]
        position++
        return result
    }

    fun consume(input: Char) {
        if (!isNext(input)) {
            throw RuntimeException("$input is not next in $tokens at $position")
        }
        next()
    }

    fun consume(input: String) {
        previousPosition = position
        if (!isNext(input)) {
            throw RuntimeException("$input is not next in $tokens at $position")
        }
        position += input.length
    }

    fun nextWhile(predicate: (Char) -> Boolean) : String {
        previousPosition = position
        for (i in position until tokens.length) {
            if (!predicate(tokens[i])) {
                val result = tokens.substring(position, i)
                position = i
                return result
            }
        }

        // Predicate always true
        val result = tokens.substring(position)
        position = tokens.length
        return result
    }

    /**
     * Returns the largest match out of the possible input strings. If no match is found, null
     * is returned.
     */
    fun nextLargest(possible:Collection<String>) : String? {
        val next = possible.sortedBy { -it.length }.firstOrNull(this::isNext)

        // Bet this part can be written more cleverly
        if (next != null) consume(next)
        return next
    }

    /**
     * Returns the largest match out of the possible input strings. If no match is found, null
     * is returned.
     */
    fun <T> nextLargest(possible:Map<String, T>) : T? {
        val token = possible.keys.sortedBy { -it.length }.firstOrNull(this::isNext)
        if (token != null) {
            consume(token)
        }
        return  possible[token]
    }

    fun peekRest() : String {
        return tokens.substring(position)
    }

    /**
     * Consumes the rest of the tokens
     */
    fun rest(): String {
        previousPosition = position
        val token = tokens.substring(position)
        position = tokens.length
        return token
    }
}