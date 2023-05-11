package com.bt.booking.domain

import java.io.Serializable
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.*

/**
 * A Phone.
 */

@Entity
@Table(name = "phone")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Phone(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,

    @get: NotNull

    @Column(name = "name", nullable = false, unique = true)
    var name: String? = null,

    @get: NotNull

    @Column(name = "brand", nullable = false)
    var brand: String? = null,

    @get: NotNull

    @Column(name = "device", nullable = false)
    var device: String? = null,

    @Column(name = "booked_on")
    var bookedOn: Instant? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
    var bookedBy: User? = null

    fun bookedBy(user: User?): Phone {
        this.bookedBy = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Phone) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Phone{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", brand='" + brand + "'" +
            ", device='" + device + "'" +
            ", bookedOn='" + bookedOn + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
