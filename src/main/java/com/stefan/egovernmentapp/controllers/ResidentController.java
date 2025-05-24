package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.dtos.ResidentDto;
import com.stefan.egovernmentapp.services.ResidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RestController
@RequestMapping("residents")
public class ResidentController {
    private final ResidentService residentService;

    @GetMapping("current")
    public ResponseEntity<ResidentDto> getCurrentResident(@RequestHeader("Authorization") String token) {
        return residentService.getCurrentResident(token);
    }
}
