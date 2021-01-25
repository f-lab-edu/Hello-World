package me.soo.helloworld.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpResponses {
    public static final ResponseEntity<Void> HTTP_RESPONSE_OK = new ResponseEntity<>(HttpStatus.OK);
    public static final ResponseEntity<Void> HTTP_RESPONSE_CREATED = new ResponseEntity<>(HttpStatus.CREATED);
    public static final ResponseEntity<Void> HTTP_RESPONSE_CONFLICT = new ResponseEntity<>(HttpStatus.CONFLICT);
}
