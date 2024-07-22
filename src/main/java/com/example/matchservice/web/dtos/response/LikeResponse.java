package com.example.matchservice.web.dtos.response;

import com.example.matchservice.web.dtos.aux.UserResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeResponse {
    private Long userId;

    private Long likedUserId;

    private boolean match;

    private UserResponse user;
}
