package com.example.matchservice.service.Impl;

import com.example.matchservice.mapper.LikeMapper;
import com.example.matchservice.persistence.entities.Like;
import com.example.matchservice.persistence.repositories.ILikeRepository;
import com.example.matchservice.service.ILikeService;
import com.example.matchservice.service.IMatchService;
import com.example.matchservice.web.dtos.aux.UserResponse;
import com.example.matchservice.web.dtos.response.BaseResponse;
import com.example.matchservice.web.dtos.response.LikeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements ILikeService {
    @Autowired
    private ILikeRepository repository;

    @Autowired
    private LikeMapper mapper;

    @Autowired
    private IMatchService matchService;

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public BaseResponse create(Long userId, Long likedUserId) {
        Like like = mapper.toEntity(userId, likedUserId);
        repository.save(like);

        Optional<Like> reciprocalLike = repository.findByUserIdAndAndLikedUserId(likedUserId, userId);
        if (reciprocalLike.isPresent()){
            matchService.create(userId, likedUserId);
            return BaseResponse.builder()
                    .data(mapper.toDto(like, Boolean.TRUE))
                    .message("It\'s a match!")
                    .success(Boolean.TRUE)
                    .statusCode(201)
                    .httpStatus(HttpStatus.CREATED)
                    .build();
        }else {
            return BaseResponse.builder()
                    .data(mapper.toDto(like, Boolean.FALSE))
                    .message("Like registered, waiting for reciprocity.")
                    .success(Boolean.TRUE)
                    .statusCode(201)
                    .httpStatus(HttpStatus.CREATED)
                    .build();
        }
    }

    @Override
    public BaseResponse getAllByUserId(Long userId) {
        return null;
    }

    @Override
    public BaseResponse getAllByLikedUserId(Long likedUserId) {
        List<Like> likes = repository.findUnmatchedLikes(likedUserId);
        if (likes.isEmpty()) {
            return BaseResponse.builder()
                    .data(Collections.emptyList())
                    .message("Likes received not found.")
                    .success(Boolean.TRUE)
                    .statusCode(200)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        List<Mono<LikeResponse>> responseMonos = likes.stream()
                .map(like -> webClient
                        .get()
                        .uri("/user/interest/" + like.getUserId())
                        .retrieve()
                        .bodyToMono(BaseResponse.class)
                        .map(baseResponse -> {
                            if (baseResponse.getSuccess()) {
                                UserResponse user = objectMapper.convertValue(baseResponse.getData(), UserResponse.class);
                                return mapper.toDtoWithUser(like, Boolean.FALSE, user);
                            } else {
                                return mapper.toDto(like, Boolean.FALSE);
                            }
                        }))
                .collect(Collectors.toList());

        List<LikeResponse> likeResponses = responseMonos.stream()
                .map(Mono::block)
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(likeResponses)
                .message("Likes received.")
                .success(Boolean.TRUE)
                .statusCode(200)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Transactional
    @Override
    public void deleteByLikedUserId(Long userId, Long likedUserId) {
        repository.deleteByUserIdAndLikedUserId(userId, likedUserId);
    }

    @Override
    public boolean verifyLike(Long userId, Long likedUserId) {
        Optional<Like> like = repository.getLike(userId, likedUserId);
        if (like.isPresent()){
            return true;
        }
        return false;
    }
}
