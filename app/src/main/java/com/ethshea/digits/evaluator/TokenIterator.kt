package com.ethshea.digits.evaluator

import java.util.*

/**
 * @author Ethan
 */
class TokenIterator(tokens: String) {
    private var position: Int = 0
    private var tokens = tokens.replace("""\s*""".toRegex(), "")

    fun hasNext() = position < tokens.length
    val remaining get() = tokens.length - position
    fun peek() = tokens[position]
    fun isNext(test: String) : Boolean {
        return remaining >= test.length
                && tokens.subSequence(position, position + test.length) == test
    }

    fun next() : Char {
        val result = tokens[position]
        position++
        return result
    }

    fun consume(input: String) {
        if (!isNext(input)) {
            throw RuntimeException("$input is not next in $tokens at $position")
        }
        position += input.length
    }
}