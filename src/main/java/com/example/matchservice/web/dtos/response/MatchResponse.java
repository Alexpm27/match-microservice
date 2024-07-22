package com.example.matchservice.web.dtos.response;

import com.example.matchservice.web.dtos.aux.UserResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchResponse {
    private Long userIdMatch;

    private UserResponse userResponse;

    public MatchResponse(Long userIdMatch, UserResponse userResponse) {
        this.userIdMatch = userIdMatch;
        this.userResponse = userResponse;
    }
}
