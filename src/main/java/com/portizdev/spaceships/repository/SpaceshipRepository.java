package com.portizdev.spaceships.repository;

import com.portizdev.spaceships.model.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {
    List<Spaceship> findByNameContaining(String name);
}
