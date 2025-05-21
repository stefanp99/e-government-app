package com.stefan.egovernmentapp.dtos;

import com.stefan.egovernmentapp.models.Resident;
import lombok.Builder;

@Builder
public record ResidentDto(Integer id,
                          String name,
                          String address,
                          UserDto userDto,
                          String personalIdNumber) {
    public static ResidentDto toDto(Resident resident) {
        return ResidentDto.builder()
                .id(resident.getId())
                .name(resident.getName())
                .address(resident.getAddress())
                .userDto(UserDto.toDto(resident.getUser()))
                .personalIdNumber(resident.getPersonalIdNumber())
                .build();
    }
}
