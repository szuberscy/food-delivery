package com.fszuberski.core.domain

import com.fszuberski.fooddelivery.registration.common.ValidationException
import com.fszuberski.fooddelivery.registration.core.domain.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserTest {

    @Test
    fun `should create user given uuid, valid name and surname`() {
        val id = UUID.randomUUID()
        val user = User(id, "name", "surname")

        assertEquals(id, user.id)
        assertEquals("name", user.name)
        assertEquals("surname", user.surname)
    }

    @Test
    fun `should create user given valid name and surname`() {
        val user = User("name", "surname")

        assertNotNull(user.id)
        assertEquals("name", user.name)
        assertEquals("surname", user.surname)
    }

    @Test
    fun `should throw validation exception given blank name`() {
        val validationException = assertThrows<ValidationException> {
            User("", "surname")
        }
        assertEquals(1, validationException.errors.size)
    }

    @Test
    fun `should throw validation exception given name contains whitespaces only`() {
        val validationException = assertThrows<ValidationException> {
            User(" \n \t", "surname")
        }
        assertEquals(1, validationException.errors.size)
    }

    @Test
    fun `should throw validation exception given blank surname`() {
        val validationException = assertThrows<ValidationException> {
            User("name", "")
        }
        assertEquals(1, validationException.errors.size)
    }

    @Test
    fun `should throw validation exception given surname contains whitespaces only`() {
        val validationException = assertThrows<ValidationException> {
            User("name", " \n \t")
        }
        assertEquals(1, validationException.errors.size)
    }

    @Test
    fun `should throw validation exception given name and surname are invalid`() {
        val validationException = assertThrows<ValidationException> {
            User("", "")
        }
        assertEquals(2, validationException.errors.size)
    }
}