package com.hangman.hangman_apis.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String wordToGuess;
    private int remainingGuesses;

    @ElementCollection
    private Set<Character> correctGuesses = new HashSet<>();

    @ElementCollection
    private Set<Character> incorrectGuesses = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "game_room_id")
    private GameRoom gameRoom;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private User player;

    int score;

}
