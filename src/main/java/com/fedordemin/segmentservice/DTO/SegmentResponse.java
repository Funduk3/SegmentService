package com.fedordemin.segmentservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SegmentResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
}
