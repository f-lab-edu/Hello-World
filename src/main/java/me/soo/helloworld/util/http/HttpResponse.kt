package me.soo.helloworld.util.http

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class HttpResponse {

    companion object {

        val OK = ResponseEntity<Unit>(HttpStatus.OK)

        val CONFLICT = ResponseEntity<Unit>(HttpStatus.CONFLICT)
    }
}
