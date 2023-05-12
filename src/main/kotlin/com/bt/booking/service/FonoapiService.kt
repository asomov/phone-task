package com.bt.booking.service

import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class FonoapiService(
    private val restTemplate: RestTemplate,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    // curl https://fonoapi.freshpixl.com/v1/getdevice -XPOST -H 'Accept: application/json' -d 'token=YOUR_TOKEN_HERE&limit=5&device=A8'
    fun callFonoapi(brand: String, device: String): FonoResponse {

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)

        val request: HttpEntity<String> =
            HttpEntity<String>("token=YOUR_TOKEN_HERE&limit=5&device=A8", headers)
        val url = "https://fonoapi.freshpixl.com/v1/getdevice"

        val result: ResponseEntity<FonoResponse> = restTemplate.postForEntity(
            url, request,
            FonoResponse::class.java
        )

        return result.body
    }
}
