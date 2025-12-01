package engineer.filip.hoarder

import engineer.filip.hoarder.data.model.Bookmark
import org.junit.Test

class KotlinPlayground {

    @Test
    fun demoVariables() {
        val immutable = "Can't change me"
        // immutable = "Error!"

        var mutable = "Can change me"
        mutable = "Changed!"

        val intInferred = 42
        val explicit: Double = 3.14

        val name = "Kotlin"
        println("Hello, $name!")
        println("Length: ${name.length}")

        println("immutable: $immutable")
        println("mutable: $mutable")
    }

    /**
     * Exercise 1: Variables
     *
     * 1. Create an immutable variable, your name
     * 2. Create a mutable variable, your age
     * 3. Try to change name after declaring it
     * 4. Change `age` to 100
     * 5. Print both
     */
    @Test
    fun variableExercise() {
        println("=== Variable Exercise ===")
    }

    @Test
    fun demoNullSafety() {
        println("=== Null Safety Demo ===")

        val nullable: String? = null
        val notNull: String = "Hello"

        // Safe call ?.
        val length = nullable?.length
        println("nullable?.length = $length")

        // Elvis operator ?:
        val safeLength = nullable?.length ?: 0
        println("nullable?.length ?: 0 = $safeLength")

        // val dangerous = nullable!!.length // Would crash!

        // Smart cast after null check
        if (nullable != null) {
            println("Length: ${nullable.length}")  // Smart cast to String
        }
    }

    /**
     * Exercise 2: Null Safety
     *
     * 1. Create a nullable string that's null
     * 2. Use safe call to get its length
     * 3. Use elvis to return 0 if null
     * 4. Print the result
     *
     */
    @Test
    fun nullSafetyExercise() {
        println("=== Null Safety Exercise ===")
        // TODO: Your code here
    }

    @Test
    fun demoDataClasses() {
        println("=== Data Classes Demo ===")

        // equals, hashCode, toString, copy
        data class Person(val name: String, val age: Int)

        val alice = Person("Alice", 30)
        println("Created: $alice")  // Auto toString()

        val olderAlice = alice.copy(age = 31)
        println("Original: $alice")
        println("Copy: $olderAlice")

        val anotherAlice = Person("Alice", 30)
        println("alice == anotherAlice: ${alice == anotherAlice}")  // true
    }

    /**
     * Exercise 3: Data Classes
     *
     * 1. Create a bookmark with any values
     * 2. Use `copy()` to create a new version with different title
     * 3. Print both to verify the original didn't change
     *
     * Extra: add `createdAt: Long = System.currentTimeMillis()` to the Bookmark data class
     */
    @Test
    fun dataClassExercise() {
        println("=== Data Class Exercise ===")
    }

    @Test
    fun demoExtensionFunctions() {
        println("=== Extension Functions Demo ===")

        // fun exclaim(text: String) = "$text!"
        fun String.exclaim() = "$this!"

        fun String.isValidEmail() = contains("@") && contains(".")

        println("Hello".exclaim())  // "Hello!"
        println("test@example.com".isValidEmail())  // true
        println("invalid".isValidEmail())  // false

        // Extension on Int
        fun Int.isEven() = this % 2 == 0
        println("4.isEven() = ${4.isEven()}")  // true
        println("7.isEven() = ${7.isEven()}")  // false
    }

    /**
     * Exercise 4: Extension Functions
     *
     * 1. First, add the isValidUrl() extension to Bookmark.kt:
     *    fun Bookmark.isValidUrl(): Boolean {
     *        return url.startsWith("http://") || url.startsWith("https://")
     *    }
     *
     * 2. Create a bookmark with a valid URL
     * 3. Create one with invalid URL (e.g., "not-a-url")
     * 4. Call `isValidUrl()` on both
     * 5. Print results
     */
    @Test
    fun extensionFunctionExercise() {
        println("=== Extension Function Exercise ===")
        // TODO: Your code here
    }

    @Test
    fun demoScopeFunctions() {
        println("=== Scope Functions Demo ===")

        // let - transform and return result
        val name: String? = "Kotlin"
        val upperName = name?.let {
            println("Inside let: $it")
            it.uppercase()
        }
        println("let result: $upperName")

        // apply - configure object and return it
        val builder = StringBuilder().apply {
            append("Hello")
            append(" ")
            append("World")
        }
        println("apply result: $builder")

        // also - do something and return original
        val numbers = mutableListOf(1, 2, 3).also {
            println("Created list with ${it.size} items")
        }
        println("also result: $numbers")

        // run - like let but uses this instead of it
        val length = "Hello".run {
            println("Running on: $this")
            length
        }
        println("run result: $length")
    }

    /**
     * Exercise 5: Scope Functions
     *
     * 1. Create a nullable bookmark (set it to `null` first)
     * 2. Use `?.let` to print its title if it exists
     * 3. Create a real bookmark
     * 4. Use `let` to get and print its title length
     * 5. Use `apply` to print a log message
     *
     * Examples:
     * - let: transforms value and returns result
     *   val length = name?.let { it.length }
     *
     * - apply: configures object and returns the object itself
     *   val person = Person().apply { name = "Alice" }
     */
    @Test
    fun scopeFunctionsExercise() {
        println("=== Scope Functions Exercise ===")
        // TODO: Your code here
    }

    @Test
    fun demoLambdas() {
        println("=== Lambdas Demo ===")

        val numbers = listOf(1, 2, 3, 4, 5)

        // map - transform each element
        val doubled = numbers.map { it * 2 }
        println("Doubled: $doubled")

        // filter - keep elements matching predicate
        val evens = numbers.filter { it % 2 == 0 }
        println("Evens: $evens")

        // Explicit parameter name
        val odds = numbers.filter { n -> n % 2 != 0 }
        println("Odds: $odds")

        // Chaining
        val result = numbers
            .filter { it > 2 }
            .map { it * 10 }
        println("Filtered and mapped: $result")

        // forEach
        numbers.forEach { print("$it ") }
        println()
    }

}
