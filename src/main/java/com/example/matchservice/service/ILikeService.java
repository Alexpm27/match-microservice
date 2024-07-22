package com.example.matchservice.service;

import com.example.matchservice.web.dtos.response.BaseResponse;

public interface ILikeService {
    BaseResponse create(Long userId, Long likedUserId);

    BaseResponse getAllByUserId(Long userId);

    BaseResponse getAllByLikedUserId(Long likedUserId);

    void deleteByLikedUserId(Long userId, Long likedUserId);

    boolean verifyLike(Long userId, Long likedUserId);
}
