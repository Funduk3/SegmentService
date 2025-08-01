package com.fedordemin.segmentservice.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListRequest {
    private List<Long> userIds;
}
