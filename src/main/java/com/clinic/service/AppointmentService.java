package com.clinic.service;


import com.clinic.dto.AppointmentDTO;
import com.clinic.dto.AppointmentRequestDTO;
import com.clinic.dto.DoctorDTO;
import com.clinic.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    private DoctorService doctorService;

    public AppointmentDTO bookAppointment(AppointmentRequestDTO request) throws Exception {
        if (isDoctorUnavailable(request.getDoctorId(), request.getAppointmentTime())) {
            throw new Exception("Doctor is not available at the requested time.");
        }

        if (isPatientDoubleBooked(request.getPatientId(), request.getAppointmentTime())) {
            throw new Exception("Patient is already booked at the requested time.");
        }

        AppointmentDTO appointment = new AppointmentDTO()
                .setId(UUID.randomUUID())
                .setAppointmentType(request.getAppointmentType())
                .setStatus("Scheduled")
                .setDoctorId(request.getDoctorId())
                .setPatientId(request.getPatientId())
                .setAppointmentTime(request.getAppointmentTime());
        distributeAppointments();
        appointmentRepository.save(appointment);
        return appointment;
    }

    private boolean isPatientDoubleBooked(UUID patientId, LocalDateTime appointmentTime) {
        List<AppointmentDTO> patientAppointments = getAppointmentsByPatientId(patientId);
        for (AppointmentDTO existing : patientAppointments) {
            if (existing.getAppointmentTime().isEqual(appointmentTime) ||
                    existing.getAppointmentTime().isBefore(appointmentTime.plusMinutes(30)) &&
                            existing.getAppointmentTime().plusMinutes(30).isAfter(appointmentTime)) {
                return true;
            }
        }
        return false;
    }

    // Check if the doctor is available at the requested time
    private boolean isDoctorUnavailable(UUID doctorId, LocalDateTime appointmentTime) {
        List<AppointmentDTO> existingAppointments = getAppointmentsByDoctorId(doctorId);
        for (AppointmentDTO existing : existingAppointments) {
            if (existing.getAppointmentTime().isEqual(appointmentTime) ||
                    existing.getAppointmentTime().isBefore(appointmentTime.plusMinutes(30)) &&
                            existing.getAppointmentTime().plusMinutes(30).isAfter(appointmentTime)) {
                return true;
            }
        }
        return false;
    }

    // Retrieve all appointments for a doctor by their ID
    public List<AppointmentDTO> getAppointmentsByDoctorId(UUID doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Retrieve all appointments
    public AppointmentDTO getAppointmentById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new Exception("Appointment not found"));
    }

    // Retrieve all appointments for a specific patient
    public List<AppointmentDTO> getAppointmentsByPatientId(UUID patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // Distribute appointments evenly among doctors and handle rescheduling
    public void distributeAppointments() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        for (DoctorDTO doctor : doctors) {
            List<AppointmentDTO> appointments = getAppointmentsByDoctorId(doctor.getId());
            if (appointments.size() > doctor.getMaxAppointmentsPerDay()) {
                rescheduleAppointments(doctor);
            }
        }
    }

    // Reschedule appointments to avoid back-to-back appointments or if doctor exceeds limits
    private void rescheduleAppointments(DoctorDTO doctor) {
        List<AppointmentDTO> appointments = getAppointmentsByDoctorId(doctor.getId());
        for (int i = 0; i < appointments.size() - 1; i++) {
            AppointmentDTO current = appointments.get(i);
            AppointmentDTO next = appointments.get(i + 1);

            if (current.getAppointmentTime().plusMinutes(30).isAfter(next.getAppointmentTime())) {
                LocalDateTime newTime = findNextAvailableTime(doctor.getId(), next.getAppointmentTime());
                if (newTime != null) {
                    next.setAppointmentTime(newTime);
                    appointmentRepository.save(next);
                }
            }
        }
    }

    // Find the next available time slot for a doctor
    private LocalDateTime findNextAvailableTime(UUID doctorId, LocalDateTime appointmentTime) {
        LocalDateTime nextAvailable = appointmentTime.plusMinutes(30);
        while (isDoctorUnavailable(doctorId, nextAvailable)) {
            nextAvailable = nextAvailable.plusMinutes(30);
            if (nextAvailable.isAfter(LocalDateTime.now().plusDays(7))) {
                return null;
            }
        }
        return nextAvailable;
    }

    // Cancel Appointment
    public void cancelAppointment(UUID appointmentId, UUID patientId, boolean isAdmin) {
        AppointmentDTO appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new Exception("Appointment not found"));

        if (!isAdmin && !appointment.getPatientId().equals(patientId)) {
            throw new IllegalArgumentException("Patient is not authorized to cancel this appointment.");
        }

        appointmentRepository.delete(appointment);
    }

    // Create appointment
    public AppointmentDTO createAppointment(AppointmentRequestDTO requestDTO) throws Exception {
        return bookAppointment(requestDTO);
    }


}
