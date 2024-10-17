package com.clinic.controller;

import com.clinic.dto.AppointmentDTO;
import com.clinic.dto.AppointmentRequestDTO;
import com.clinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // View all appointments for a specific patient
    @GetMapping("/{patientId}/appointments")
    public ResponseEntity<List<AppointmentDTO>> getPatientAppointments(@PathVariable UUID patientId) {
        List<AppointmentDTO> appointments = patientService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    // Book an appointment (patients can use this endpoint or AppointmentController's booking method)
    @PostMapping("/{patientId}/appointments")
    public ResponseEntity<AppointmentDTO> bookAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO) throws Exception {
        appointmentRequestDTO.setPatientId(appointmentRequestDTO.getPatientId());
        AppointmentDTO bookedAppointment = patientService.bookAppointment(appointmentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookedAppointment);
    }

    // Cancel an appointment for the patient
    @DeleteMapping("/{patientId}/appointments/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable UUID patientId, @PathVariable UUID appointmentId, Boolean isAdmin) {
        patientService.cancelAppointment(patientId, appointmentId, isAdmin);
        return ResponseEntity.noContent().build();
    }
}
