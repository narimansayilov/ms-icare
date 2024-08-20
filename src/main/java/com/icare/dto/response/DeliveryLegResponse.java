package com.icare.dto.response;

import com.icare.dto.jsonB.Location;
import com.icare.dto.jsonB.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryLegResponse {
    private Measurement distance;
    private Measurement duration;
    private String startAddress;
    private String endAddress;
    private Location startLocation;
    private Location endLocation;
}
