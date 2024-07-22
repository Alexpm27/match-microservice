package com.example.matchservice.service.Impl;

import com.example.matchservice.persistence.entities.Match;
import com.example.matchservice.persistence.repositories.IMatchRepository;
import com.example.matchservice.service.IMatchService;
import com.example.matchservice.web.dtos.aux.UserResponse;
import com.example.matchservice.web.dtos.response.BaseResponse;
import com.example.matchservice.web.dtos.response.MatchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements IMatchService {
    @Autowired
    private IMatchRepository repository;

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Match create(Long user1Id, Long user2Id) {
        Match match = new Match();
        match.setUser1Id(user1Id);
        match.setUser2Id(user2Id);
        return repository.save(match);
    }

    @Override
    public BaseResponse getAllByUserId(Long userId) {
        List<Match> matches = repository.findAllMatchesByUserId(userId);
        if (matches.isEmpty()) {
            return BaseResponse.builder()
                    .data(matches)
                    .message("No matches were found for the user.")
                    .success(Boolean.TRUE)
                    .statusCode(200)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        List<Mono<MatchResponse>> responseMonos = matches.stream()
                .map(match -> {
                    Long otherUserId = match.getUser1Id().equals(userId) ? match.getUser2Id() : match.getUser1Id();
                    return webClient
                            .get()
                            .uri("/user/interest/" + otherUserId)
                            .retrieve()
                            .bodyToMono(BaseResponse.class)
                            .map(baseResponse -> {
                                if (baseResponse.getSuccess()) {
                                    UserResponse user = objectMapper.convertValue(baseResponse.getData(), UserResponse.class);
                                    return new MatchResponse(match.getId(), user);
                                } else {
                                    return new MatchResponse(match.getId(), null);
                                }
                            });
                })
                .toList();

        // Combinar los resultados de los matches y usuarios
        return Mono.zip(responseMonos, results -> {
            List<MatchResponse> matchResponses = Arrays.stream(results)
                    .map(result -> (MatchResponse) result)
                    .toList();
            return BaseResponse.builder()
                    .data(matchResponses)
                    .message("Matches found.")
                    .success(Boolean.TRUE)
                    .statusCode(200)
                    .httpStatus(HttpStatus.OK)
                    .build();
        }).block();
    }

    @Transactional
    @Override
    public void deleteByUserId(Long user1Id, Long user2Id) {
        repository.deleteByUserId(user1Id, user2Id);
    }
}
