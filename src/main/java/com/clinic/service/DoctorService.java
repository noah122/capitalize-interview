package com.clinic.service;

import com.clinic.dto.AppointmentDTO;
import com.clinic.dto.DoctorDTO;
import src.main.java.com.clinic.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentService appointmentService;

    // Get all doctors
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Get doctor by ID
    public Optional<DoctorDTO> getDoctorById(UUID id) {
        return doctorRepository.findById(id);
    }

    // Get doctor's appointments for the day
    public List<AppointmentDTO> getDoctorAppointments(UUID doctorId) {
        return appointmentService.getAppointmentsByDoctorId(doctorId);
    }

    // Update doctor's availability
    public void updateAvailability(UUID doctorId, List<LocalDateTime> unavailableHours) {
        doctorRepository.findById(doctorId)
                .map(doctor -> {
                    doctor.setUnavailableHours(unavailableHours);
                    return doctorRepository.save(doctor);
                })
                .orElseThrow(() -> new Exception("Doctor not found"));
    }


    public void approveAppointment(UUID doctorId, UUID appointmentId) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(appointmentId);

        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new IllegalArgumentException("Doctor is not authorized to approve this appointment.");
        }

        appointment.setStatus("Approved");

        appointmentService.saveAppointment(appointment);
    }

    // Cancel Appointment
    public void cancelAppointment(UUID doctorId, UUID appointmentId) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(appointmentId);

        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new IllegalArgumentException("Doctor is not authorized to reject this appointment.");
        }

         appointment.setStatus("Cancelled");

        appointmentService.saveAppointment(appointment);
    }
}

