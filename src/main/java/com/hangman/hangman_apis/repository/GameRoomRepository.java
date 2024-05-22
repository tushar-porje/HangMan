package com.hangman.hangman_apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hangman.hangman_apis.entity.GameRoom;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
}
