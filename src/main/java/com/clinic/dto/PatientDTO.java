package com.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    @Id
    private UUID id;

    private String name;

    private String email;

    private String phoneNumber;

    private List<AppointmentDTO> appointments;
}

