package com.clinic.service;

import com.clinic.dto.AppointmentDTO;
import com.clinic.dto.AppointmentRequestDTO;
import com.clinic.dto.PatientDTO;
import com.clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentService appointmentService;

    // Get all patients
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get patient by ID
    public PatientDTO getPatientById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new Exception("Patient not found"));
    }

    // Book an appointment for a patient
    public AppointmentDTO bookAppointment(AppointmentRequestDTO requestDTO) throws Exception {
        return appointmentService.createAppointment(requestDTO);
    }

    // Cancel an appointment
    public void cancelAppointment(UUID appointmentId, UUID patientId, Boolean isAdmin) {
        appointmentService.cancelAppointment(appointmentId, patientId, isAdmin);
    }

    // Get all patient appointments
    public List<AppointmentDTO> getPatientAppointments(UUID patientId) {
        return appointmentService.getAppointmentsByPatientId(patientId);
    }


}

