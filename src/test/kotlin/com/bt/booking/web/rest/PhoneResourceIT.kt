package com.bt.booking.web.rest

import com.bt.booking.IntegrationTest
import com.bt.booking.domain.Phone
import com.bt.booking.repository.PhoneRepository
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

/**
 * Integration tests for the [PhoneResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhoneResourceIT {
    @Autowired
    private lateinit var phoneRepository: PhoneRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restPhoneMockMvc: MockMvc

    private lateinit var phone: Phone

    @BeforeEach
    fun initTest() {
        phone = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createPhone() {
        val databaseSizeBeforeCreate = phoneRepository.findAll().size
        // Create the Phone
        restPhoneMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(phone))
        ).andExpect(status().isCreated)

        // Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate + 1)
        val testPhone = phoneList[phoneList.size - 1]

        assertThat(testPhone.name).isEqualTo(DEFAULT_NAME)
        assertThat(testPhone.brand).isEqualTo(DEFAULT_BRAND)
        assertThat(testPhone.device).isEqualTo(DEFAULT_DEVICE)
        assertThat(testPhone.bookedOn).isEqualTo(DEFAULT_BOOKED_ON)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createPhoneWithExistingId() {
        // Create the Phone with an existing ID
        phone.id = 1L

        val databaseSizeBeforeCreate = phoneRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(phone))
        ).andExpect(status().isBadRequest)

        // Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = phoneRepository.findAll().size
        // set the field null
        phone.name = null

        // Create the Phone, which fails.

        restPhoneMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(phone))
        ).andExpect(status().isBadRequest)

        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkBrandIsRequired() {
        val databaseSizeBeforeTest = phoneRepository.findAll().size
        // set the field null
        phone.brand = null

        // Create the Phone, which fails.

        restPhoneMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(phone))
        ).andExpect(status().isBadRequest)

        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkDeviceIsRequired() {
        val databaseSizeBeforeTest = phoneRepository.findAll().size
        // set the field null
        phone.device = null

        // Create the Phone, which fails.

        restPhoneMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(phone))
        ).andExpect(status().isBadRequest)

        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllPhones() {
        // Initialize the database
        phoneRepository.saveAndFlush(phone)

        // Get all the phoneList
        restPhoneMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phone.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE)))
            .andExpect(jsonPath("$.[*].bookedOn").value(hasItem(DEFAULT_BOOKED_ON.toString())))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getPhone() {
        // Initialize the database
        phoneRepository.saveAndFlush(phone)

        val id = phone.id
        assertNotNull(id)

        // Get the phone
        restPhoneMockMvc.perform(get(ENTITY_API_URL_ID, phone.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phone.id?.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.device").value(DEFAULT_DEVICE))
            .andExpect(jsonPath("$.bookedOn").value(DEFAULT_BOOKED_ON.toString()))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingPhone() {
        // Get the phone
        restPhoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putExistingPhone() {
        // Initialize the database
        phoneRepository.saveAndFlush(phone)

        val databaseSizeBeforeUpdate = phoneRepository.findAll().size

        // Update the phone
        val updatedPhone = phoneRepository.findById(phone.id).get()
        // Disconnect from session so that the updates on updatedPhone are not directly saved in db
        em.detach(updatedPhone)
        updatedPhone.name = UPDATED_NAME
        updatedPhone.brand = UPDATED_BRAND
        updatedPhone.device = UPDATED_DEVICE
        updatedPhone.bookedOn = UPDATED_BOOKED_ON

        restPhoneMockMvc.perform(
            put(ENTITY_API_URL_ID, updatedPhone.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedPhone))
        ).andExpect(status().isOk)

        // Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate)
        val testPhone = phoneList[phoneList.size - 1]
        assertThat(testPhone.name).isEqualTo(UPDATED_NAME)
        assertThat(testPhone.brand).isEqualTo(UPDATED_BRAND)
        assertThat(testPhone.device).isEqualTo(UPDATED_DEVICE)
        assertThat(testPhone.bookedOn).isEqualTo(UPDATED_BOOKED_ON)
    }

    @Test
    @Transactional
    fun putNonExistingPhone() {
        val databaseSizeBeforeUpdate = phoneRepository.findAll().size
        phone.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneMockMvc.perform(
            put(ENTITY_API_URL_ID, phone.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(phone))
        )
            .andExpect(status().isBadRequest)

        // Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchPhone() {
        val databaseSizeBeforeUpdate = phoneRepository.findAll().size
        phone.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(phone))
        ).andExpect(status().isBadRequest)

        // Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamPhone() {
        val databaseSizeBeforeUpdate = phoneRepository.findAll().size
        phone.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(phone))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdatePhoneWithPatch() {
        phoneRepository.saveAndFlush(phone)

        val databaseSizeBeforeUpdate = phoneRepository.findAll().size

// Update the phone using partial update
        val partialUpdatedPhone = Phone().apply {
            id = phone.id

            device = UPDATED_DEVICE
        }

        restPhoneMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedPhone.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedPhone))
        )
            .andExpect(status().isOk)

// Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate)
        val testPhone = phoneList.last()
        assertThat(testPhone.name).isEqualTo(DEFAULT_NAME)
        assertThat(testPhone.brand).isEqualTo(DEFAULT_BRAND)
        assertThat(testPhone.device).isEqualTo(UPDATED_DEVICE)
        assertThat(testPhone.bookedOn).isEqualTo(DEFAULT_BOOKED_ON)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdatePhoneWithPatch() {
        phoneRepository.saveAndFlush(phone)

        val databaseSizeBeforeUpdate = phoneRepository.findAll().size

// Update the phone using partial update
        val partialUpdatedPhone = Phone().apply {
            id = phone.id

            name = UPDATED_NAME
            brand = UPDATED_BRAND
            device = UPDATED_DEVICE
            bookedOn = UPDATED_BOOKED_ON
        }

        restPhoneMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedPhone.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedPhone))
        )
            .andExpect(status().isOk)

// Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate)
        val testPhone = phoneList.last()
        assertThat(testPhone.name).isEqualTo(UPDATED_NAME)
        assertThat(testPhone.brand).isEqualTo(UPDATED_BRAND)
        assertThat(testPhone.device).isEqualTo(UPDATED_DEVICE)
        assertThat(testPhone.bookedOn).isEqualTo(UPDATED_BOOKED_ON)
    }

    @Throws(Exception::class)
    fun patchNonExistingPhone() {
        val databaseSizeBeforeUpdate = phoneRepository.findAll().size
        phone.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneMockMvc.perform(
            patch(ENTITY_API_URL_ID, phone.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(phone))
        )
            .andExpect(status().isBadRequest)

        // Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchPhone() {
        val databaseSizeBeforeUpdate = phoneRepository.findAll().size
        phone.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(phone))
        )
            .andExpect(status().isBadRequest)

        // Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamPhone() {
        val databaseSizeBeforeUpdate = phoneRepository.findAll().size
        phone.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(phone))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Phone in the database
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deletePhone() {
        // Initialize the database
        phoneRepository.saveAndFlush(phone)
        val databaseSizeBeforeDelete = phoneRepository.findAll().size
        // Delete the phone
        restPhoneMockMvc.perform(
            delete(ENTITY_API_URL_ID, phone.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val phoneList = phoneRepository.findAll()
        assertThat(phoneList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_BRAND = "AAAAAAAAAA"
        private const val UPDATED_BRAND = "BBBBBBBBBB"

        private const val DEFAULT_DEVICE = "AAAAAAAAAA"
        private const val UPDATED_DEVICE = "BBBBBBBBBB"

        private val DEFAULT_BOOKED_ON: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_BOOKED_ON: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val ENTITY_API_URL: String = "/api/phones"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Phone {
            val phone = Phone(
                name = DEFAULT_NAME,

                brand = DEFAULT_BRAND,

                device = DEFAULT_DEVICE,

                bookedOn = DEFAULT_BOOKED_ON

            )

            return phone
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Phone {
            val phone = Phone(
                name = UPDATED_NAME,

                brand = UPDATED_BRAND,

                device = UPDATED_DEVICE,

                bookedOn = UPDATED_BOOKED_ON

            )

            return phone
        }
    }
}
