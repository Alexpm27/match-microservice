package com.example.matchservice.web.dtos.aux;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class UserResponse {
    private Long id;

    private String name;

    private String gender;

    private String profileUrl;


    //private List<InterestResponse> interests;
}
