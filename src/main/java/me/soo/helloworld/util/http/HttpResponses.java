package me.soo.helloworld.util.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpResponses {

    public static final ResponseEntity<Void> HTTP_RESPONSE_OK = new ResponseEntity<>(HttpStatus.OK);

    public static final ResponseEntity<Void> HTTP_RESPONSE_CREATED = new ResponseEntity<>(HttpStatus.CREATED);

    public static final ResponseEntity<Void> HTTP_RESPONSE_CONFLICT = new ResponseEntity<>(HttpStatus.CONFLICT);

    public static final ResponseEntity<Void> HTTP_RESPONSE_UNAUTHORIZED = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    public static final ResponseEntity<Void> HTTP_RESPONSE_NO_CONTENT = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    public static final ResponseEntity<Void> HTTP_RESPONSE_NOT_FOUND = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    public static final ResponseEntity<Void> HTTP_RESPONSE_INTERNAL_SERVER_ERROR
            = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
}
