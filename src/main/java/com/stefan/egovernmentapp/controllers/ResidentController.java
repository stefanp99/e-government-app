package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.dtos.ComplaintDto;
import com.stefan.egovernmentapp.exceptions.NoResidentFoundException;
import com.stefan.egovernmentapp.services.ResidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor

@RestController
@RequestMapping("residents")
public class ResidentController {
    private final ResidentService residentService;
    @GetMapping("complaints")
    public CompletableFuture<List<ComplaintDto>> getComplaintsByResidentId(@RequestParam("residentId") Integer residentId){
        try {
            return residentService.findComplaintsByResidentId(residentId);
        } catch (NoResidentFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
