package com.jd.adapter.in;

import com.jd.application.port.in.EventUseCase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * @author Jaedoo Lee
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventUseCase eventUseCase;

    @PostMapping
    public ResponseEntity<Void> event(@Valid @RequestBody EventRequest request) {

        eventUseCase.event(request);
        return ResponseEntity.created(URI.create("/events")).build();
    }

}
