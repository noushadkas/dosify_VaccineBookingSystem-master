package com.example.Dosify.transformer;

import com.example.Dosify.dto.ResponseDto.CertificateResponseDto;
import com.example.Dosify.model.Dose1_Certificate;
import com.example.Dosify.model.Dose2_Certificate;

public class CertificateTransformer {
    public static CertificateResponseDto CertificateToCertificateResponseDto(Dose1_Certificate dose1_certificate){
        return CertificateResponseDto.builder()
                .vaccinationStatus(dose1_certificate.getVaccinationStatus())
                .gender(dose1_certificate.getGender())
                .beneficiaryName(dose1_certificate.getBeneficiaryName())
                .age(dose1_certificate.getAge())
                .emailId(dose1_certificate.getEmailId())
                .beneficiaryRefId(dose1_certificate.getBeneficiaryRefId())
                .vaccineType(dose1_certificate.getVaccineType())
                .dateDose(dose1_certificate.getDateOfFirstDose())
                .doctorName(dose1_certificate.getDoctorName())
                .centerName(dose1_certificate.getCenterName())
                .build();
    }

    public static CertificateResponseDto CertificateToCertificateResponseDto(Dose2_Certificate dose2_certificate){
        return CertificateResponseDto.builder()
                .vaccinationStatus(dose2_certificate.getVaccinationStatus())
                .gender(dose2_certificate.getGender())
                .beneficiaryName(dose2_certificate.getBeneficiaryName())
                .age(dose2_certificate.getAge())
                .emailId(dose2_certificate.getEmailId())
                .certificateId(dose2_certificate.getCertificateId())
                .beneficiaryRefId(dose2_certificate.getBeneficiaryRefId())
                .vaccineType(dose2_certificate.getVaccineType())
                .dateDose(dose2_certificate.getDateOfSecondDose())
                .doctorName(dose2_certificate.getDoctorName())
                .centerName(dose2_certificate.getCenterName())
                .build();
    }
}