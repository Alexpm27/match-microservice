package com.example.matchservice.service;

import com.example.matchservice.persistence.entities.Match;
import com.example.matchservice.web.dtos.response.BaseResponse;

public interface IMatchService {
    Match create(Long user1Id, Long user2Id);

    BaseResponse getAllByUserId(Long userId);

    void deleteByUserId(Long user1Id, Long user2Id);
}
