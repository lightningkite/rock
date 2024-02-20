package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.utilities

/**
 *
 * Created by joseph on 10/23/17.
 */
data class FuzzySearch(val charSets: List<Map<Char, Char>>) {

    companion object {

        val lookalikes = HashMap<Char, ArrayList<Char>>().apply {
            val groups = listOf(listOf('0', 'O', 'o', 'q', 'Q'), listOf('1', 'I', 'l', 'i', '!'))
            for (group in groups) {
                for (a in group) {
                    for (b in group) {
                        if (a == b) continue
                        getOrPut(a) { ArrayList() }.add(b)
                    }
                }
            }
            for (lowercase in 'a'..'z') {
                val uppercase = lowercase.toUpperCase()
                getOrPut(lowercase) { ArrayList() }.add(uppercase)
                getOrPut(uppercase) { ArrayList() }.add(lowercase)
            }
        }

        fun fromStrings(sets: List<String>): FuzzySearch {
            return FuzzySearch(sets.map { it.toLookalikeMap() })
        }

        private val cached = HashMap<String, Map<Char, Char>>()
        private fun String.toLookalikeMap(): Map<Char, Char> = cached.getOrPut(this) {
            val current = HashMap<Char, Char>()
            for (c in this) {
                lookalikes[c]?.forEach { lookalike ->
                    current[lookalike] = c
                }
            }
            for (c in this) {
                current[c] = c
            }
            return current
        }
    }

    data class Match(val changes: Int = 0, val startsAt: Int = 0, val string: String = "")

    fun match(str: String): List<Match> {
        val results = ArrayList<Match>()
        val builder = StringBuilder(charSets.size)
        possibilities@ for (possibleIndex in 0..str.length - charSets.size) {
            builder.clear()
            var changes = 0
            for (setIndex in charSets.indices) {
                val direct = str[setIndex + possibleIndex]
                val matching = charSets[setIndex][direct] ?: continue@possibilities
                builder.append(matching)
                if (matching != direct) changes++
            }
            results.add(Match(changes = changes, startsAt = possibleIndex, string = builder.toString()))
        }
        return results.sortedBy { it.changes }
    }
}

val vinLetterOrNumber = "0123456789ABCDEFGHJKLMNPRSTUVWXYZ"
val vinCheckDigit = "0123456789X"
val vinSequentialDigit = "0123456789"
val vinFuzzySearch = FuzzySearch.fromStrings(
    listOf(
        // 1XP 5DB9X 0 7D 669 120
        //Manufacturer identifier
        vinLetterOrNumber,
        vinLetterOrNumber,
        vinLetterOrNumber,
        //Vehicle Attributes
        vinLetterOrNumber,
        vinLetterOrNumber,
        vinLetterOrNumber,
        vinLetterOrNumber,
        vinLetterOrNumber,
        //Check Digit
        vinCheckDigit,
        //Model Year
        vinLetterOrNumber,
        //Plant Code
        vinLetterOrNumber,
        //Manufacturer Identifier / Sequential Start
        vinLetterOrNumber,
        vinLetterOrNumber,
        vinLetterOrNumber,
        //Sequential End
        vinSequentialDigit,
        vinSequentialDigit,
        vinSequentialDigit
    )
)


private fun transliterate(c: Char): Int {
    return "0123456789.ABCDEFGH..JKLMN.P.R..STUVWXYZ".indexOf(c) % 10
}

private val VIN_WEIGHTS = listOf(8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2)
private fun getCheckDigit(vin: String): Char {
    var sum = 0
    for (i in 0..16) {
        sum += transliterate(vin[i]) * VIN_WEIGHTS[i]
    }
    val map = "0123456789X"
    return map[sum % 11]
}

private fun validate(vin: String): Boolean {
    return if (vin.length != 17) false else getCheckDigit(vin) == vin[8]
}

/**
 * Checks if the string is a valid VIN.
 * This even includes usage of the check digit.
 */
fun String.isValidVin() = validate(this.uppercase())