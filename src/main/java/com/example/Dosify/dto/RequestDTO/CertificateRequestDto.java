package com.example.Dosify.dto.RequestDto;

import com.example.Dosify.Enum.DoseNo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateRequestDto {
    int userId;

    DoseNo doseNo;
}