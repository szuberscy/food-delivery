package com.fszuberski.fooddelivery.registration.core.domain

import com.fszuberski.fooddelivery.registration.common.ValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

private const val BCRYPT_PATTERN = "^\\$2[ayb]\\$.{56}$"

class UserRegistrationTest {

    @Test
    fun `should instantiate valid UserRegistration`() {
        val result = UserRegistration(
            "name",
            "surname",
            "test@example.com",
            "57rO%nG_p4S\$w0Rd"
        )

        assertEquals("name", result.name)
        assertEquals("surname", result.surname)
        assertEquals("test@example.com", result.email)

        assertNotEquals("57rO%nG_p4S\$w0Rd", result.passwordHash)
        assertTrue { result.passwordHash.matches(Regex(BCRYPT_PATTERN)) }
    }

    @Test
    fun `should throw validation exception if name is blank`() {
        val result = assertThrows<ValidationException>{
            UserRegistration(
                "",
                "surname",
                "test@example.com",
                "57rO%nG_p4S\$w0Rd"
            )
        }

        assertEquals(1, result.errors.size)
        assertEquals("name cannot be blank", result.errors.first().error)
    }

    @Test
    fun `should throw validation exception if surname is blank`() {
        val result = assertThrows<ValidationException>{
            UserRegistration(
                "name",
                "",
                "test@example.com",
                "57rO%nG_p4S\$w0Rd"
            )
        }

        assertEquals(1, result.errors.size)
        assertEquals("surname cannot be blank", result.errors.first().error)
    }

    @Test
    fun `should throw validation exception if email is blank`() {
        val result = assertThrows<ValidationException>{
            UserRegistration(
                "name",
                "surname",
                "",
                "57rO%nG_p4S\$w0Rd"
            )
        }

        assertEquals(1, result.errors.size)
        assertEquals("invalid email", result.errors.first().error)
    }

    @Test
    fun `should throw validation exception if email is invalid`() {
        val result = assertThrows<ValidationException>{
            UserRegistration(
                "name",
                "surname",
                "invalid_email@",
                "57rO%nG_p4S\$w0Rd"
            )
        }

        assertEquals(1, result.errors.size)
        assertEquals("invalid email", result.errors.first().error)
    }

    @Test
    fun `should throw validation exception if password contains less than 8 characters`() {
        val result = assertThrows<ValidationException> {
            UserRegistration(
                "name",
                "surname",
                "test@example.com",
                "abc123"
            )
        }

        assertEquals(1, result.errors.size)
        assertEquals("password must contain at least 8 characters", result.errors.first().error)
    }

    @Test
    fun `should throw validation exception if password doesn't contain a letter`() {
        val result = assertThrows<ValidationException> {
            UserRegistration(
                "name",
                "surname",
                "test@example.com",
                "123#*67@"
            )
        }

        assertEquals(1, result.errors.size)
        assertEquals("password must contain at least one letter", result.errors.first().error)
    }

    @Test
    fun `should throw validation exception if password doesn't contain a digit`() {
        val result = assertThrows<ValidationException> {
            UserRegistration(
                "name",
                "surname",
                "test@example.com",
                "passWORD"
            )
        }

        assertEquals(1, result.errors.size)
        assertEquals("password must contain at least one digit", result.errors.first().error)
    }

    @Test
    fun `should throw validation exception with multiple errors if multiple parameters are invalid`() {
        val result = assertThrows<ValidationException> {
            UserRegistration(
                "",
                "",
                "invalid_email@",
                " __!@#\n\t "
            )
        }

        assertEquals(6, result.errors.size)
    }
}