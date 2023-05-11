package com.bt.booking.domain

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import com.bt.booking.web.rest.equalsVerifier

import java.util.UUID

class PhoneTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Phone::class)
        val phone1 = Phone()
        phone1.id = 1L
        val phone2 = Phone()
        phone2.id = phone1.id
        assertThat(phone1).isEqualTo(phone2)
        phone2.id = 2L
        assertThat(phone1).isNotEqualTo(phone2)
        phone1.id = null
        assertThat(phone1).isNotEqualTo(phone2)
    }
}
