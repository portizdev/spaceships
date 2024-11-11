package com.portizdev.spaceships;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.portizdev.spaceships.model.Spaceship;
import com.portizdev.spaceships.repository.SpaceshipRepository;
import com.portizdev.spaceships.service.SpaceshipService;
import com.portizdev.spaceships.kafka.SpaceshipProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class SpaceshipServiceTest {

    @Mock
    private SpaceshipRepository repository;

    @Mock
    private SpaceshipProducer producer; 

    @InjectMocks
    private SpaceshipService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSpaceships() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Spaceship testSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        Page<Spaceship> page = new PageImpl<>(Collections.singletonList(testSpaceship));
        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<Spaceship> result = service.getAllSpaceships(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testSpaceship, result.getContent().get(0));
        verify(repository).findAll(pageable);
    }

    @Test
    void testGetSpaceshipById() {
        // Given
        Spaceship testSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        when(repository.findById(1L)).thenReturn(Optional.of(testSpaceship));

        // When
        Spaceship result = service.getSpaceshipById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testSpaceship, result);
        verify(repository).findById(1L);
    }

    @Test
    void testSearchSpaceshipsByName() {
        // Given
        Spaceship testSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        List<Spaceship> spaceships = Collections.singletonList(testSpaceship);
        when(repository.findByNameContaining("Falcon")).thenReturn(spaceships);

        // When
        List<Spaceship> result = service.searchSpaceshipsByName("Falcon");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSpaceship, result.get(0));
        verify(repository).findByNameContaining("Falcon");
    }

    @Test
    void testCreateSpaceship() {
        // Given
        Spaceship testSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        when(repository.save(testSpaceship)).thenReturn(testSpaceship);

        // When
        Spaceship result = service.createSpaceship(testSpaceship);

        // Then
        assertNotNull(result);
        assertEquals(testSpaceship, result);
        verify(repository).save(testSpaceship);
        verify(producer).sendSpaceshipEvent(testSpaceship, "CREATED");
    }

    @Test
    void testUpdateSpaceship() {
        // Given
        Spaceship existingSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        Spaceship updatedSpaceship = new Spaceship(1L, "FalconX", "Cargo", 150);
        when(repository.findById(1L)).thenReturn(Optional.of(existingSpaceship));
        when(repository.save(updatedSpaceship)).thenReturn(updatedSpaceship);

        // When
        Spaceship result = service.updateSpaceship(1L, updatedSpaceship);

        // Then
        assertNotNull(result);
        assertEquals(updatedSpaceship, result);
        verify(repository).save(updatedSpaceship);
        verify(producer).sendSpaceshipEvent(updatedSpaceship, "UPDATED");
    }

    @Test
    void testDeleteSpaceship() {
        // Given
        Spaceship testSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        when(repository.findById(1L)).thenReturn(Optional.of(testSpaceship));

        // When
        service.deleteSpaceship(1L);

        // Then
        verify(repository).deleteById(1L);
        verify(producer).sendSpaceshipEvent(argThat(spaceship -> spaceship.getId().equals(1L)), eq("DELETED"));
    }
}
