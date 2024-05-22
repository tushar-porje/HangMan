package com.hangman.hangman_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hangman.hangman_apis.entity.GameRoom;
import com.hangman.hangman_apis.entity.GameState;
import com.hangman.hangman_apis.service.GameRoomService;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameRoomService gameRoomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/room")
    public ResponseEntity<GameRoom> createGameRoom(@RequestParam String name, @RequestParam String username) {
        try {
            GameRoom gameRoom = gameRoomService.createGameRoom(name, username);
            return ResponseEntity.ok(gameRoom);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/room/{gameRoomId}/join")
    public ResponseEntity<GameRoom> joinGameRoom(@PathVariable Long gameRoomId, @RequestParam String username) {
        try {
            GameRoom gameRoom = gameRoomService.joinGameRoom(gameRoomId, username);
            return ResponseEntity.ok(gameRoom);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/room/{gameRoomId}/assign-wordmaster")
    public ResponseEntity<GameRoom> assignWordMaster(@PathVariable Long gameRoomId, @RequestParam String username) {
        try {
            GameRoom gameRoom = gameRoomService.assignWordMaster(gameRoomId, username);
            return ResponseEntity.ok(gameRoom);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @MessageMapping("/start")
    public ResponseEntity<Void> startGame(@RequestParam Long gameRoomId, @RequestParam String wordToGuess, @RequestParam String username) {
        try {
            GameState gameState = gameRoomService.startGame(gameRoomId, wordToGuess, username);
            messagingTemplate.convertAndSend("/topic/game/" + gameRoomId, gameState);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @MessageMapping("/guess")
    public ResponseEntity<Void> makeGuess(@RequestParam Long gameStateId, @RequestParam char guess) {
        try {
            GameState gameState = gameRoomService.makeGuess(gameStateId, guess);
            messagingTemplate.convertAndSend("/topic/game/" + gameState.getGameRoom().getId(), gameState);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}