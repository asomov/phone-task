package com.bt.booking.domain

import com.bt.booking.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
