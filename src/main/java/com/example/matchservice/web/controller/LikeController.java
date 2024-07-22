package com.example.matchservice.web.controller;

import com.example.matchservice.service.ILikeService;
import com.example.matchservice.web.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("like")
public class LikeController {
    @Autowired
    private ILikeService service;

    @PostMapping("/{userId}/{likedUserId}")
    public ResponseEntity<BaseResponse> create(@PathVariable Long userId, @PathVariable Long likedUserId){
        return new ResponseEntity<>(service.create(userId, likedUserId), HttpStatus.OK);
    }

    @GetMapping("/likes/{likedUserId}")
    public ResponseEntity<BaseResponse> getAllByLikedUserId(@PathVariable Long likedUserId){
        return new ResponseEntity<>(service.getAllByLikedUserId(likedUserId), HttpStatus.OK);
    }

    @GetMapping("/verify/{userId}/{likedUserId}")
    public ResponseEntity<Boolean> verifyLike(@PathVariable Long userId, @PathVariable Long likedUserId){
        return new ResponseEntity<>(service.verifyLike(userId,likedUserId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/{likedUserId}")
    public void deleteByLikedUserId(@PathVariable Long userId, @PathVariable Long likedUserId){
        service.deleteByLikedUserId(userId, likedUserId);
    }
}
