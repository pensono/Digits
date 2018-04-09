package com.ethshea.digits.evaluator

import java.util.*

/**
 * @author Ethan
 */
class TokenIterator(tokens: String) {
    var position: Int = 0
        private set
    private var tokens = tokens.replace("""\s*""".toRegex(), "")
    private var speculations = Stack<Int>()

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
        if (!isNext(input)) {
            throw RuntimeException("$input is not next in $tokens at $position")
        }
        position += input.length
    }

    /**
     * Returns the largest match out of the possible input strings. If no match is found, null
     * is returned.
     */
    fun nextLargest(possible:Collection<String>) : String? {
        return possible.sortedBy { -it.length }.firstOrNull(this::isNext)
    }

    /**
     * Like {@link #nextLargest} but it also consumes the token
     */
    fun takeNextLargest(possible:Collection<String>) : String? {
        val next = nextLargest(possible)
        // Bet this part can be written more cleverly
        if (next != null) consume(next)
        return next
    }

    /**
     * Returns the largest match out of the possible input strings. If no match is found, null
     * is returned.
     */
    fun <T> nextLargest(possible:Map<String, T>) : T? {
        return possible[possible.keys.sortedBy { -it.length }.firstOrNull(this::isNext)]
    }

    /**
     * Like {@link #nextLargest} but it also consumes the token
     */
    fun <T> takeNextLargest(possible:Map<String, T>) : T? {
        val next = nextLargest(possible.keys)
        // Bet this part can be written more cleverly
        if (next != null) {
            consume(next)
            return possible[next]
        }
        return next
    }

    /**
     * Save the current position
     */
    fun speculate() {

    }

    /**
     * Commit to the current speculation
     */
    fun commit(){

    }

    /**
     * Back out of speculation
     */
    fun fial() {}

}