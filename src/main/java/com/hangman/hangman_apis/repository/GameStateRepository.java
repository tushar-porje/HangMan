package com.hangman.hangman_apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hangman.hangman_apis.entity.GameState;

public interface GameStateRepository extends JpaRepository<GameState, Long> {
}