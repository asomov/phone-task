package com.bt.booking.web.rest

import com.bt.booking.domain.Phone
import com.bt.booking.repository.PhoneRepository
import com.bt.booking.service.PhoneService
import com.bt.booking.web.rest.errors.BadRequestAlertException

import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.validation.Valid
import javax.validation.constraints.NotNull
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "phone"
/**
 * REST controller for managing [com.bt.booking.domain.Phone].
 */
@RestController
@RequestMapping("/api")
class PhoneResource(
        private val phoneService: PhoneService,
        private val phoneRepository: PhoneRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "phone"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /phones` : Create a new phone.
     *
     * @param phone the phone to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new phone, or with status `400 (Bad Request)` if the phone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/phones")
    fun createPhone(@Valid @RequestBody phone: Phone): ResponseEntity<Phone> {
        log.debug("REST request to save Phone : $phone")
        if (phone.id != null) {
            throw BadRequestAlertException(
                "A new phone cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = phoneService.save(phone)
            return ResponseEntity.created(URI("/api/phones/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /phones/:id} : Updates an existing phone.
     *
     * @param id the id of the phone to save.
     * @param phone the phone to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated phone,
     * or with status `400 (Bad Request)` if the phone is not valid,
     * or with status `500 (Internal Server Error)` if the phone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/phones/{id}")
    fun updatePhone(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody phone: Phone
    ): ResponseEntity<Phone> {
        log.debug("REST request to update Phone : {}, {}", id, phone)
        if (phone.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, phone.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }


        if (!phoneRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = phoneService.update(phone)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, false, ENTITY_NAME,
                     phone.id.toString()
                )
            )
            .body(result)
    }

    /**
    * {@code PATCH  /phones/:id} : Partial updates given fields of an existing phone, field will ignore if it is null
    *
    * @param id the id of the phone to save.
    * @param phone the phone to update.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phone,
    * or with status {@code 400 (Bad Request)} if the phone is not valid,
    * or with status {@code 404 (Not Found)} if the phone is not found,
    * or with status {@code 500 (Internal Server Error)} if the phone couldn't be updated.
    * @throws URISyntaxException if the Location URI syntax is incorrect.
    */
    @PatchMapping(value = ["/phones/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdatePhone(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody phone:Phone
    ): ResponseEntity<Phone> {
        log.debug("REST request to partial update Phone partially : {}, {}", id, phone)
        if (phone.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, phone.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!phoneRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }



            val result = phoneService.partialUpdate(phone)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phone.id.toString())
        )
    }

    /**
     * `GET  /phones` : get all the phones.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of phones in body.
     */
    @GetMapping("/phones")    
    fun getAllPhones(): MutableList<Phone> {
        
        

            log.debug("REST request to get all Phones")
            
            return phoneService.findAll()
                }

    /**
     * `GET  /phones/:id` : get the "id" phone.
     *
     * @param id the id of the phone to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the phone, or with status `404 (Not Found)`.
     */
    @GetMapping("/phones/{id}")
    fun getPhone(@PathVariable id: Long): ResponseEntity<Phone> {
        log.debug("REST request to get Phone : $id")
        val phone = phoneService.findOne(id)
        return ResponseUtil.wrapOrNotFound(phone)
    }
    /**
     *  `DELETE  /phones/:id` : delete the "id" phone.
     *
     * @param id the id of the phone to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/phones/{id}")
    fun deletePhone(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Phone : $id")

        phoneService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build()
    }
}
