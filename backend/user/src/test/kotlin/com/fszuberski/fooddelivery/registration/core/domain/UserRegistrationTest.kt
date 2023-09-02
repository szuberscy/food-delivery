package com.fszuberski.fooddelivery.registration.core.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

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
        assertTrue { result.passwordHash.matches(Regex("^\\$2[ayb]\\$.{56}$")) }
    }
}