package com.stefan.egovernmentapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "complaints")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "resident_id", referencedColumnName = "id")
    private Resident resident;
    @Column(name = "information")
    private String information;
    @ManyToOne
    @JoinColumn(name = "complaint_type_id", referencedColumnName = "id")
    private ComplaintType complaintType;
    @Column(name = "added_date")
    private Timestamp addedDate;
    @Column(name = "status")
    private String status;
}
