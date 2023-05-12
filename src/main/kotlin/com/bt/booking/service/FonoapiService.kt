package com.bt.booking.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class FonoapiService(
    @Value("\${fonoapi.url}") private val url: String,
    @Value("\${fonoapi.token}") private val token: String,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    // curl https://fonoapi.freshpixl.com/v1/getdevice -XPOST -H 'Accept: application/json' -d 'token=YOUR_TOKEN_HERE&limit=5&device=A8'
    fun callFonoapi(brand: String, device: String): FonoResponse {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON) // -H 'Accept: application/json'

        val request: HttpEntity<String> =
            HttpEntity<String>("token=$token&brand=$brand&device=$device", headers)

        val result: ResponseEntity<FonoResponse> = restTemplate.postForEntity(
            url, request,
            FonoResponse::class.java
        )
        return result.body
    }
}
