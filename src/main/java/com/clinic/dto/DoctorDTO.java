package com.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {

    @Id
    private UUID id;

    private String name;

    private String specialization;

    private Integer maxAppointmentsPerDay;

    private List<AppointmentDTO> appointments;

    private List<LocalDateTime> unavailableHours;
}

