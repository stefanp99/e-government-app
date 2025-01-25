package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.dtos.AddedComplaintResidentDto;
import com.stefan.egovernmentapp.dtos.ComplaintDto;
import com.stefan.egovernmentapp.exceptions.NoComplaintException;
import com.stefan.egovernmentapp.exceptions.NoKnownStatusException;
import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.services.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("complaints")
public class ComplaintController {
    private final ComplaintService complaintService;

    @PostMapping("add-complaint")
    public Complaint addComplaint(@RequestBody AddedComplaintResidentDto addedComplaintDto){
        return complaintService.addComplaint(addedComplaintDto);
    }

    @PatchMapping("{status}")
    public ComplaintDto changeStatus(@PathVariable String status, @RequestParam("complaintId") Integer complaintId) throws NoComplaintException {
        return complaintService.updateComplaintStatus(complaintId, status);
    }

    @GetMapping("{status}")
    public List<ComplaintDto> complaintsByStatus(@PathVariable String status) throws NoKnownStatusException {
        return complaintService.findComplaintsByStatus(status);
    }

    @ExceptionHandler(NoKnownStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNoKnownStatusException(NoKnownStatusException ex) {
        return ex.getMessage();
    }
}
