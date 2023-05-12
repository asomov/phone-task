package com.bt.booking.service

import com.bt.booking.domain.Phone
import com.bt.booking.repository.PhoneRepository
import com.bt.booking.service.dto.PhoneDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [Phone].
 */
@Service
@Transactional
class PhoneService(
    private val phoneRepository: PhoneRepository,
    private val fonoapiService: FonoapiService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a phone.
     *
     * @param phone the entity to save.
     * @return the persisted entity.
     */
    fun save(phone: Phone): Phone {
        log.debug("Request to save Phone : $phone")
        return phoneRepository.save(phone)
    }

    /**
     * Update a phone.
     *
     * @param phone the entity to save.
     * @return the persisted entity.
     */
    fun update(phone: Phone): Phone {
        log.debug("Request to update Phone : {}", phone)
        return phoneRepository.save(phone)
    }

    /**
     * Partially updates a phone.
     *
     * @param phone the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(phone: Phone): Optional<Phone> {
        log.debug("Request to partially update Phone : {}", phone)

        return phoneRepository.findById(phone.id)
            .map {

                if (phone.name != null) {
                    it.name = phone.name
                }
                if (phone.brand != null) {
                    it.brand = phone.brand
                }
                if (phone.device != null) {
                    it.device = phone.device
                }
                if (phone.bookedOn != null) {
                    it.bookedOn = phone.bookedOn
                }

                it
            }
            .map { phoneRepository.save(it) }
    }

    /**
     * Get all the phones.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(): List<PhoneDTO> {
        log.debug("Request to get all Phones")
        return phoneRepository.findAll().map { ph -> enrich(ph) }
    }

    /**
     * Get one phone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<PhoneDTO> {
        log.debug("Request to get Phone : $id")
        return phoneRepository.findById(id).map { ph -> enrich(ph) }
    }

    /**
     * Delete the phone by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete Phone : $id")

        phoneRepository.deleteById(id)
    }

    fun enrich(phone: Phone): PhoneDTO {
        val fonodata = try {
            fonoapiService.callFonoapi(phone.brand!!, phone.device!!)
        } catch (e: Exception) {
            FonoResponse(
                "Not available",
                "Not available",
                "Not available",
                "Not available"
            )
        }
        return PhoneDTO(phone, fonodata)
    }
}
