package com.clinic.controller;

import com.clinic.dto.AppointmentDTO;
import com.clinic.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // View all appointments for a specific doctor
    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<AppointmentDTO>> getDoctorAppointments(@PathVariable UUID doctorId) {
        List<AppointmentDTO> appointments = doctorService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointments);
    }

    // Mark a doctor as unavailable for a specific time range
    @PostMapping("/{doctorId}/{time}")
    public ResponseEntity<Void> markDoctorUnavailable(@PathVariable UUID doctorId,
                                                      @RequestBody List<LocalDateTime> unavailableHours) {
        doctorService.updateAvailability(doctorId, unavailableHours);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{doctorId}/approve/{appointmentId}")
    public ResponseEntity<Void> approveAppointment(@PathVariable UUID doctorId, @PathVariable UUID appointmentId) {
        doctorService.approveAppointment(doctorId, appointmentId);
        return ResponseEntity.ok().build();
    }

    // Cancel an appointment
    @DeleteMapping("/{doctorId}/appointments/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable UUID doctorId, @PathVariable UUID appointmentId) {
        doctorService.cancelAppointment(doctorId, appointmentId);
        return ResponseEntity.noContent().build();
    }
}
