package com.example.matchservice.web.controller;

import com.example.matchservice.service.IMatchService;
import com.example.matchservice.web.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("match")
public class MatchController {
    @Autowired
    private IMatchService service;

    @GetMapping("/matches/{userId}")
    public ResponseEntity<BaseResponse> getAllByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(service.getAllByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{user1Id}/{user2Id}")
    public void deleteByLikedUserId(@PathVariable Long user1Id, @PathVariable Long user2Id){
        service.deleteByUserId(user1Id, user2Id);
    }
}
