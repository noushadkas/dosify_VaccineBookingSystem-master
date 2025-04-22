package com.example.Dosify.service.impl;

import com.example.Dosify.Enum.DoseNo;
import com.example.Dosify.Enum.VaccinationStatus;
import com.example.Dosify.dto.RequestDto.CertificateRequestDto;
import com.example.Dosify.dto.ResponseDto.CertificateResponseDto;
import com.example.Dosify.exception.UserDoseNotTakenException;
import com.example.Dosify.exception.UserNotFoundException;
import com.example.Dosify.model.Appointment;
import com.example.Dosify.model.Dose1_Certificate;
import com.example.Dosify.model.Dose2_Certificate;
import com.example.Dosify.model.User;
import com.example.Dosify.repository.Dose1_CertificateRepository;
import com.example.Dosify.repository.Dose2_CertificateRepository;
import com.example.Dosify.repository.UserRepository;
import com.example.Dosify.service.CertificateService;
import com.example.Dosify.transformer.CertificateTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class CertificateServiceImp implements CertificateService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Dose1_CertificateRepository dose1_certificateRepository;

    @Autowired
    Dose2_CertificateRepository dose2_certificateRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public CertificateResponseDto addCertificate(CertificateRequestDto certificateRequestDto) throws UserNotFoundException, UserDoseNotTakenException {
        /*Check user exist or not*/
        Optional<User> userOpt = userRepository.findById(certificateRequestDto.getUserId());
        if (userOpt.isEmpty()){
            throw new UserNotFoundException("User doesn't exist.");
        }

        User user = userOpt.get();
        List<Appointment> appointmentList = user.getAppointments();
        String dName = "", cName = "";
        Date d = new Date();
        for (Appointment app:appointmentList) {
            User getUser = app.getUser();
            DoseNo doseNo = app.getDoseNo();
            if(getUser.getId() == certificateRequestDto.getUserId() && doseNo == certificateRequestDto.getDoseNo()){
                dName = app.getDoctor().getName();
                cName = app.getDoctor().getVaccinationCenter().getName();
                d = app.getDateOfAppointment();
            }
        }

        /* Check user dose taken or not*/
        if (!user.isDose1Taken()){
            throw new UserDoseNotTakenException("Sorry! You are not taken any dose.");
        }else {
            /*Prepare certificate entity*/
            Dose1_Certificate certificate = Dose1_Certificate.builder()
                    .beneficiaryName(user.getName())
                    .age(user.getAge())
                    .emailId(user.getEmailId())
                    .beneficiaryRefId(String.valueOf(UUID.randomUUID()))
                    .vaccineType(user.getDose1().getVaccineType())
                    .dateOfFirstDose(d)
                    .doctorName(dName)
                    .centerName(cName)
                    .user(user)
                    .build();

            /*Saved into database*/
            Dose1_Certificate savedCertificate = dose1_certificateRepository.save(certificate);

            /*Certificate(entity) --> Dto*/
            return CertificateTransformer.CertificateToCertificateResponseDto(savedCertificate);
        }
    }

    @Override
    public void createDoseCertificate(User user, DoseNo doseNo) {
        List<Appointment> appointmentList = user.getAppointments();
        String dName = "", cName = "";
        Date d = new Date();
        for (Appointment app:appointmentList) {
            User getUser = app.getUser();
            DoseNo appDoseNo = app.getDoseNo();
            if(getUser.getId() == user.getId() && appDoseNo == doseNo){
                dName = app.getDoctor().getName();
                cName = app.getDoctor().getVaccinationCenter().getName();
                d = app.getDateOfAppointment();
            }
        }

        /*Generate random number */
        Random random = new Random();
        if (doseNo == DoseNo.DOSE_1){

            /*Prepare dose 1 certificate entity*/
            Dose1_Certificate certificate1 = Dose1_Certificate.builder()
                    .beneficiaryName(user.getName())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .emailId(user.getEmailId())
                    .vaccinationStatus(VaccinationStatus.PARTIALLY_VACCINATED)
                    .beneficiaryRefId(String.valueOf(random.nextDouble()).substring(2))
                    .vaccineType(user.getDose1().getVaccineType())
                    .dateOfFirstDose(d)
                    .doctorName(dName)
                    .centerName(cName)
                    .user(user)
                    .build();
            /*Saved into database*/
            Dose1_Certificate savedCertificate = dose1_certificateRepository.save(certificate1);
        }else{
            /*Prepare dose 2 certificate entity*/
            Dose2_Certificate certificate2 = Dose2_Certificate.builder()
                    .beneficiaryName(user.getName())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .emailId(user.getEmailId())
                    .vaccinationStatus(VaccinationStatus.FULLY_VACCINATED)
                    .beneficiaryRefId(String.valueOf(random.nextDouble()).substring(2))
                    .certificateId(String.valueOf(random.nextDouble()).substring(2, 13))
                    .vaccineType(user.getDose2().getVaccineType())
                    .dateOfSecondDose(d)
                    .doctorName(dName)
                    .centerName(cName)
                    .user(user)
                    .build();
            /*Saved into database*/
            Dose2_Certificate savedCertificate = dose2_certificateRepository.save(certificate2);
        }
    }

    @Override
    public CertificateResponseDto getCertificate(String mobNo, String doseNo) throws UserNotFoundException {
        Iterable<User> users = userRepository.findAll();
        int id = 0;
        for (User u: users) {
            if (u.getMobNo().equals(mobNo)){
                id = u.getId();
            }
        }

        /*If id is not found then throw the exception*/
        if (id == 0){
            throw new UserNotFoundException("Number is not registered on Dosify");
        }

        CertificateResponseDto certificateResponseDto;

        /*Prepare text message for male*/
        String text = "\t Certificate for COVID-19 Vaccination \n";
        String date = "";

        if (DoseNo.DOSE_1.toString().equals(doseNo)){
            Dose1_Certificate dose1_certificate = dose1_certificateRepository.findByUserId(id);
            certificateResponseDto = CertificateTransformer.CertificateToCertificateResponseDto(dose1_certificate);

            date += certificateResponseDto.getDateDose().toString();

            text += "\t" + "   " + "Partially Vaccinated : 1st Dose \n\n" +
                    "Beneficiary Details \n" + "------------------- \n" +
                    "Beneficiary Name         : " + certificateResponseDto.getBeneficiaryName() + "\n" +
                    "Age                      : " + certificateResponseDto.getAge() + "\n" +
                    "Gender                   : " + certificateResponseDto.getGender() + "\n" +
                    "Beneficiary Reference Id : " + certificateResponseDto.getBeneficiaryRefId() + "\n\n" +
                    "Vaccination Details \n" + "-------------------\n" +
                    "Vaccine Name             : " + certificateResponseDto.getVaccineType() + "\n" +
                    "Date of 1st dose         : " + date.substring(0, 10) + "\n" +
                    "Vaccinated by            : " + certificateResponseDto.getDoctorName() + "\n" +
                    "Vaccination at           : " + certificateResponseDto.getCenterName() + "\n\n";
        }else{
            Dose2_Certificate dose2_certificate = dose2_certificateRepository.findByUserId(id);
            certificateResponseDto = CertificateTransformer.CertificateToCertificateResponseDto(dose2_certificate);

            date += certificateResponseDto.getDateDose().toString();

            text += "Issued in India by Ministry of Health & Family \n" + "\t \t Welfare, Govt. of India \n " +
                    "\t"  + "   " + "Certificate ID : " + certificateResponseDto.getCertificateId() + "\n\n" +
                    "Beneficiary Details \n" + "------------------- \n" +
                    "Beneficiary Name         : " + certificateResponseDto.getBeneficiaryName() + "\n" +
                    "Age                      : " + certificateResponseDto.getAge() + "\n" +
                    "Gender                   : " + certificateResponseDto.getGender() + "\n" +
                    "Beneficiary Reference Id : " + certificateResponseDto.getBeneficiaryRefId() + "\n" +
                    "Vaccination Status       : " + "Fully Vaccinated (2 Doses)" + "\n\n" +
                    "Vaccination Details \n" + "------------------- \n" +
                    "Vaccine Name             : " + certificateResponseDto.getVaccineType() + "\n" +
                    "Date of 1st dose         : " + date.substring(0, 10) + "\n" +
                    "Vaccinated by            : " + certificateResponseDto.getDoctorName() + "\n" +
                    "Vaccination at           : " + certificateResponseDto.getCenterName() + "\n\n";
        }

        text += "Thank you!!!" + "\n" + "no-reply this is automated generated mail.";

        /*Send male to user*/
        sendCertificate(certificateResponseDto, text);
        return certificateResponseDto;
    }

    private void sendCertificate(CertificateResponseDto certificateResponseDto, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abdulecommerce@gmail.com");
        message.setTo(certificateResponseDto.getEmailId());
        message.setSubject("Dosify COVID_19 Vaccination Center!!!");
        message.setText(text);
        emailSender.send(message);
    }
}