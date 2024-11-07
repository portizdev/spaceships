package com.portizdev.spaceships.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SpaceshipConsumer {

    @KafkaListener(topics = "spaceship-events", groupId = "spaceship_group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
        //Se podría añadir cualquier acción como: sincronizar BD, registrar eventos, logs..
    }
}