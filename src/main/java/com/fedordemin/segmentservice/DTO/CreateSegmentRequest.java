package com.fedordemin.segmentservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSegmentRequest {
    private String code;
    private String name;
    private String description;
}
