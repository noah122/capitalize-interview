package com.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AppointmentDTO {

    @Id
    private UUID id;

    private UUID doctorId;

    private UUID patientId;

    private LocalDateTime appointmentTime;

    private String appointmentType;

    private String status;
}

