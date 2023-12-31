package com.example.registrationlogindemo.service.impl;

import com.example.registrationlogindemo.dto.AppointmentDto;
import com.example.registrationlogindemo.entity.Appointment;
import com.example.registrationlogindemo.entity.Doctor;
import com.example.registrationlogindemo.repository.AppointmentRepository;
import com.example.registrationlogindemo.repository.DoctorRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.AppointmentService;
import com.example.registrationlogindemo.service.DoctorService;
import com.example.registrationlogindemo.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final DoctorService doctorService;



    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, UserRepository userRepository, UserService userService, DoctorService doctorService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.doctorService = doctorService;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Optional<Appointment> getAppointmentById(Integer id) {
        return appointmentRepository.findById(id);

    }

    @Override
    public Long findDoctorIdByName(String doctorFirstName, String doctorLastName) {
        Doctor doctor = doctorRepository.findByUserFirstNameAndUserLastName(doctorFirstName, doctorLastName);
        if (doctor != null) {
            return doctor.getId();
        }
        return null;
    }

    @Override
    public List<AppointmentDto> getAppointmentsByDoctorId(Long doctorId) {
        /*List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return convertToAppointmentDtoList(appointments);*/
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor != null) {
            String firstName = doctor.getUser().getFirstName();
            String lastName = doctor.getUser().getLastName();
            List<Appointment> appointments = appointmentRepository.findByDoctor_User_FirstNameAndDoctor_User_LastName(firstName, lastName);
            List<AppointmentDto> appointmentDtos = new ArrayList<>();

            for (Appointment appointment : appointments) {
                AppointmentDto appointmentDto = new AppointmentDto();
                appointmentDto.setAppDate(appointment.getApp_date());
                appointmentDto.setUserId(appointment.getUserId());
                appointmentDto.setReason(appointment.getReason());
                appointmentDto.setStatus(appointment.getStatus());
                appointmentDtos.add(appointmentDto);
            }

            return appointmentDtos;
        }
        return Collections.emptyList();
    }


    @Override
    public List<Appointment> getAppointmentsByPatientId(Long userId) {
        return appointmentRepository.findByUserId(userId);
    }

    @Override
    public Appointment createAppointment(Appointment appointment, Long doctorId, Long userId) {
        return null;
    }


    /*@Override
    @Transactional
    public void saveAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = new Appointment();
        appointment.setApp_date(appointmentDto.getAppDate());
        appointment.setDoc_specialty(appointmentDto.getDocSpecialty());
        appointment.setReason(appointmentDto.getReason());
        // Set the selected doctor from the appointmentDto
       // appointment.setDoctor(appointmentDto.getSelectedDoctor());

        // Fetch the selected doctor from the DoctorRepository
        Doctor selectedDoctor = doctorRepository.findById(appointmentDto.getDoctorId()).orElse(null);
        appointment.setDoctor(selectedDoctor);
        if (selectedDoctor != null) {
            appointment.setDoctor_id(selectedDoctor.getId());
        }
        // Fetch the user (patient) from the UserRepository using the userId
        User user = userRepository.findById(appointmentDto.getUserId()).orElse(null);
        if (user != null) {
            appointment.setUser(user);
            appointment.setUserId(user.getId());
        }

        // Save the appointment
        appointmentRepository.save(appointment);
    }*/

    @Override
    @Transactional
    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentDto> getAppointmentsByUserId(Long userId) {
        List<Appointment> appointments = appointmentRepository.findByUserId(userId);
        return convertToAppointmentDtoList(appointments);
    }

    @Override
    public Long findDoctorIdByName(Doctor selectedDoctor) {
        return null;
    }

    @Override
    public List<AppointmentDto> getAppointmentsBySpecialty(String specialty) {
        return null;
    }


    // Create a helper method to convert List<Appointment> to List<AppointmentDto>
    private List<AppointmentDto> convertToAppointmentDtoList(List<Appointment> appointments) {
        // Implement the conversion logic here
        // For each Appointment, create an AppointmentDto and set the required fields
        // Return the list of AppointmentDto objects
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            AppointmentDto dto = new AppointmentDto();
            dto.setAppDate(appointment.getApp_date());
            dto.setUserId(appointment.getUserId());
            dto.setReason(appointment.getReason());
            dto.setStatus(appointment.getStatus());
            // Add dto to the list
            appointmentDtos.add(dto);
        }
        return appointmentDtos;
    }

    public List<Appointment> searchAppointments(String amka, String specialty) {
        // Implement the logic to search for appointments by specialty and AMKA
        // Return the list of matching appointments
        return appointmentRepository.findByUserAmkaAndDoctorSpecialty(amka, specialty);
    }

    @Override
    public List<Object[]> getAppointmentsByDateAndSpecialty(Date searchDate, String specialty) {
        // Implement the logic to search for appointments by date and specialty
        // You'll need to define a custom query method in your AppointmentRepository
        // to retrieve the desired data.

        return appointmentRepository.findAppointmentsByDateAndSpecialty(searchDate, specialty);
    }

    @Override
    public List<Appointment> getAppointmentsByPatientEmail(String email) {
        return appointmentRepository.findByUserEmail(email);
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
