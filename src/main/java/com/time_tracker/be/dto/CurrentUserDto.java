package com.time_tracker.be.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CurrentUserDto {
    private Long id_user;
    private String email;
    private String name;
    // constructor, getter, setter, etc.
}
