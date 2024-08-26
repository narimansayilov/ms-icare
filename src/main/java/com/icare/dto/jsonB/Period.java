package com.icare.dto.jsonB;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Period {
    private LocalDate startDate;
    private LocalDate endDate;
}
