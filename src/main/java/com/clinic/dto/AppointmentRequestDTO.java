package com.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    private UUID patientId;

    private UUID doctorId;

    private LocalDateTime appointmentTime;

    private String appointmentType;
}

