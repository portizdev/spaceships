package com.portizdev.spaceships;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.portizdev.spaceships.controller.SpaceshipController;
import com.portizdev.spaceships.model.Spaceship;
import com.portizdev.spaceships.service.SpaceshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.List;

class SpaceshipControllerTest {

    @Mock
    private SpaceshipService service;

    @Mock
    private PagedResourcesAssembler<Spaceship> pagedResourcesAssembler;

    @InjectMocks
    private SpaceshipController controller;

    private final String apiKeyHeader = "x-api-key";
    private final String apiKeyValue = "test-api-key"; 

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
        when(service.getAllSpaceships(pageable)).thenReturn(page);
        when(pagedResourcesAssembler.toModel(page)).thenReturn(PagedModel.empty());

        HttpHeaders headers = new HttpHeaders();
        headers.add(apiKeyHeader, apiKeyValue);

        // When
        PagedModel<?> result = controller.getAllSpaceships(pageable);

        // Then
        assertNotNull(result);
        verify(service).getAllSpaceships(pageable);
    }

    @Test
    void testGetSpaceshipById() {
        // Given
        Spaceship testSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        when(service.getSpaceshipById(1L)).thenReturn(testSpaceship);

        HttpHeaders headers = new HttpHeaders();
        headers.add(apiKeyHeader, apiKeyValue);

        // When
        Spaceship result = controller.getSpaceshipById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testSpaceship, result);
        verify(service).getSpaceshipById(1L);
    }

    @Test
    void testSearchSpaceships() {
        // Given
        Spaceship testSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        List<Spaceship> spaceships = Collections.singletonList(testSpaceship);
        when(service.searchSpaceshipsByName("Falcon")).thenReturn(spaceships);

        HttpHeaders headers = new HttpHeaders();
        headers.add(apiKeyHeader, apiKeyValue);

        // When
        List<Spaceship> result = controller.searchSpaceships("Falcon");

        // Then
        assertNotNull(result);
        assertEquals(spaceships.size(), result.size());
        verify(service).searchSpaceshipsByName("Falcon");
    }

    @Test
    void testCreateSpaceship() {
        // Given
        Spaceship testSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        when(service.createSpaceship(testSpaceship)).thenReturn(testSpaceship);

        HttpHeaders headers = new HttpHeaders();
        headers.add(apiKeyHeader, apiKeyValue);

        // When
        Spaceship result = controller.createSpaceship(testSpaceship);

        // Then
        assertNotNull(result);
        assertEquals(testSpaceship, result);
        verify(service).createSpaceship(testSpaceship);
    }

    @Test
    void testUpdateSpaceship() {
        // Given
        Spaceship testSpaceship = new Spaceship(1L, "Falcon", "Cargo", 100);
        when(service.updateSpaceship(1L, testSpaceship)).thenReturn(testSpaceship);

        HttpHeaders headers = new HttpHeaders();
        headers.add(apiKeyHeader, apiKeyValue);

        // When
        Spaceship result = controller.updateSpaceship(1L, testSpaceship);

        // Then
        assertNotNull(result);
        assertEquals(testSpaceship, result);
        verify(service).updateSpaceship(1L, testSpaceship);
    }

    @Test
    void testDeleteSpaceship() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add(apiKeyHeader, apiKeyValue);

        // When
        controller.deleteSpaceship(1L);

        // Then
        verify(service).deleteSpaceship(1L);
    }
}

