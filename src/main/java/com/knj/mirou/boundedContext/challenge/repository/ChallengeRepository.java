package com.knj.mirou.boundedContext.challenge.repository;

import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

}
