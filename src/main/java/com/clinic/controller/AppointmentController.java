package com.clinic.controller;

import com.clinic.dto.AppointmentDTO;
import com.clinic.dto.AppointmentRequestDTO;
import com.clinic.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Book a new appointment
    @PostMapping("/book")
    public ResponseEntity<AppointmentDTO> bookAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO) throws Exception {
        AppointmentDTO bookedAppointment = appointmentService.bookAppointment(appointmentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookedAppointment);
    }

    // Cancel an existing appointment by its ID
    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable UUID appointmentId, UUID patientId, Boolean isAdmin) {
        appointmentService.cancelAppointment(appointmentId, patientId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    // Get all appointments for a specific patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForPatient(@PathVariable UUID patientId) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(appointments);
    }

    // can add date functionality
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForDoctor(@PathVariable UUID doctorId) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }
}
