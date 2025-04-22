package com.example.Dosify.dto.ResponseDto;

import com.example.Dosify.Enum.Gender;
import com.example.Dosify.Enum.VaccinationStatus;
import com.example.Dosify.Enum.VaccineType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CertificateResponseDto {

    VaccinationStatus vaccinationStatus;

    Gender gender;

    String beneficiaryName;

    int age;

    String emailId;

    /*Random number*/
    String beneficiaryRefId;

    /*Random number*/
    String certificateId;

    VaccineType vaccineType;

    Date dateDose;

    String doctorName;

    String centerName;
}