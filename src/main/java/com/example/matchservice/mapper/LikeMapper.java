package com.example.matchservice.mapper;

import com.example.matchservice.persistence.entities.Like;
import com.example.matchservice.web.dtos.aux.UserResponse;
import com.example.matchservice.web.dtos.response.LikeResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LikeMapper {
    public Like toEntity(Long userId, Long likedUserId){
        Like like = new Like();
        like.setUserId(userId);
        like.setLikedUserId(likedUserId);
        return like;
    }

    public LikeResponse toDto(Like like, boolean match){
        return LikeResponse.builder()
                .userId(like.getUserId())
                .likedUserId(like.getLikedUserId())
                .match(match)
                .build();
    }

    public LikeResponse toDtoWithUser(Like like, boolean match, UserResponse user){
        return LikeResponse.builder()
                .userId(like.getUserId())
                .likedUserId(like.getLikedUserId())
                .match(match)
                .user(user)
                .build();
    }

    public List<LikeResponse> toDtoList(List<Like> likes){
        return likes.stream()
                .map(x -> toDto(x, Boolean.FALSE))
                .collect(Collectors.toList());
    }
}
