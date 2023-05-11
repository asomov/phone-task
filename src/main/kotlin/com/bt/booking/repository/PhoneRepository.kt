package com.bt.booking.repository

import com.bt.booking.domain.Phone
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Phone entity.
 */
@Suppress("unused")
@Repository
interface PhoneRepository : JpaRepository<Phone, Long> {

    @Query("select phone from Phone phone where phone.bookedBy.login = ?#{principal.username}")
    fun findByBookedByIsCurrentUser(): MutableList<Phone>
}
