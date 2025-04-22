package com.example.Dosify.service;

import com.example.Dosify.Enum.DoseNo;
import com.example.Dosify.dto.RequestDto.CertificateRequestDto;
import com.example.Dosify.dto.ResponseDto.CertificateResponseDto;
import com.example.Dosify.exception.UserDoseNotTakenException;
import com.example.Dosify.exception.UserNotFoundException;
import com.example.Dosify.model.Dose1;
import com.example.Dosify.model.User;

public interface CertificateService {
    CertificateResponseDto addCertificate(CertificateRequestDto certificateRequestDto) throws UserNotFoundException, UserDoseNotTakenException;

    void createDoseCertificate(User user, DoseNo doseNo);

    CertificateResponseDto getCertificate(String mobNo, String doseNo) throws UserNotFoundException;
}