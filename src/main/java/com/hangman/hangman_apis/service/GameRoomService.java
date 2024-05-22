package com.hangman.hangman_apis.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hangman.hangman_apis.entity.GameRoom;
import com.hangman.hangman_apis.entity.GameState;
import com.hangman.hangman_apis.entity.User;
import com.hangman.hangman_apis.repository.GameRoomRepository;
import com.hangman.hangman_apis.repository.GameStateRepository;
import com.hangman.hangman_apis.repository.UserRepository;

@Service
public class GameRoomService {

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private GameStateRepository gameStateRepository;

    @Autowired
    private UserRepository userRepository;

    public Set<User> playerInRoom(Long gameRoomId){
        Optional<GameRoom> gameRoomOptional = gameRoomRepository.findById(gameRoomId);
        if (gameRoomOptional.isPresent()) {
            return gameRoomOptional.get().getPlayers();
        }
        throw new RuntimeException("Game room not found");
    }

    public GameRoom createGameRoom(String name, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GameRoom gameRoom = new GameRoom();
        gameRoom.setName(name);
        gameRoom.setWordMaster(user);
        gameRoom.getPlayers().add(user);

        return gameRoomRepository.save(gameRoom);
    }

    public GameRoom joinGameRoom(Long gameRoomId, String username) {
        Optional<GameRoom> gameRoomOptional = gameRoomRepository.findById(gameRoomId);
        if (gameRoomOptional.isPresent()) {
            GameRoom gameRoom = gameRoomOptional.get();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            gameRoom.getPlayers().add(user);
            return gameRoomRepository.save(gameRoom);
        }
        throw new RuntimeException("Game room not found");
    }

    public void leaveGameRoom(Long gameRoomId, String username) {
        Optional<GameRoom> gameRoomOptional = gameRoomRepository.findById(gameRoomId);
        if (gameRoomOptional.isPresent()) {
            GameRoom gameRoom = gameRoomOptional.get();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            gameRoom.getPlayers().remove(user);
            gameRoomRepository.save(gameRoom);
        } else {
            throw new RuntimeException("Game room not found");
        }
    }

    public GameRoom assignWordMaster(Long gameRoomId, String username) {
        Optional<GameRoom> gameRoomOptional = gameRoomRepository.findById(gameRoomId);
        if (gameRoomOptional.isPresent()) {
            GameRoom gameRoom = gameRoomOptional.get();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            gameRoom.setWordMaster(user);
            return gameRoomRepository.save(gameRoom);
        }
        throw new RuntimeException("Game room not found");
    }

    public GameState startGame(Long gameRoomId, String wordToGuess, String username) {
        Optional<GameRoom> gameRoomOptional = gameRoomRepository.findById(gameRoomId);
        if (gameRoomOptional.isPresent()) {
            GameRoom gameRoom = gameRoomOptional.get();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            GameState gameState = new GameState();
            gameState.setWordToGuess(wordToGuess);
            gameState.setRemainingGuesses(6);
            gameState.setGameRoom(gameRoom);
            gameState.setPlayer(user);
            gameRoom.getGameStates().add(gameState);
            return gameStateRepository.save(gameState);
        }
        throw new RuntimeException("Game room not found");
    }

    public GameState makeGuess(Long gameStateId, char guess) {
        Optional<GameState> gameStateOptional = gameStateRepository.findById(gameStateId);
        if (gameStateOptional.isPresent()) {
            GameState gameState = gameStateOptional.get();
            Set<Character> correctGuesses = gameState.getCorrectGuesses();
            Set<Character> incorrectGuesses = gameState.getIncorrectGuesses();

            if (gameState.getWordToGuess().indexOf(guess) >= 0) {
                correctGuesses.add(guess);
                if (isWordGuessed(gameState)) {
                    endGame(gameState, true);
                }
            } else {
                incorrectGuesses.add(guess);
                gameState.setRemainingGuesses(gameState.getRemainingGuesses() - 1);
                if (gameState.getRemainingGuesses() <= 0) {
                    endGame(gameState, false);
                }
            }
            return gameStateRepository.save(gameState);
        }
        throw new RuntimeException("Game state not found");
    }

    private boolean isWordGuessed(GameState gameState) {
        String wordToGuess = gameState.getWordToGuess();
        for (char c : wordToGuess.toCharArray()) {
            if (!gameState.getCorrectGuesses().contains(c)) {
                return false;
            }
        }
        return true;
    }

    private void endGame(GameState gameState, boolean isWin) {
        gameState.setRemainingGuesses(0);
        gameStateRepository.save(gameState);
            //due to time constrain not able to complete
        }
    
}
