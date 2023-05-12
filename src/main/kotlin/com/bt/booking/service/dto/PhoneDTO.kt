package com.bt.booking.service.dto

import com.bt.booking.domain.Phone
import com.bt.booking.domain.User
import java.io.Serializable
import java.time.Instant

data class PhoneDTO(
    val id: Long,
    val name: String,
    val brand: String,
    val device: String,
    val bookedOn: Instant?,
    val bookedBy: User?,
    val technology: String,
    val bands2g: String,
    val bands3g: String,
    val bands4g: String,
) : Serializable {
    constructor(
        phone: Phone,
        technology: String,
        bands2g: String,
        bands3g: String,
        bands4g: String
    ) : this(
        phone.id!!, phone.name!!, phone.brand!!,
        phone.device!!, phone.bookedOn, phone.bookedBy, technology, bands2g, bands3g, bands4g
    )
}
