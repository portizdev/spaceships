package com.portizdev.spaceships.kafka;


import com.portizdev.spaceships.model.Spaceship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpaceshipProducer {

    private static final String TOPIC = "spaceship-events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendSpaceshipEvent(Spaceship spaceship, String action) {
        String message = String.format("Action: %s, ID: %s, Name: %s", action, spaceship.getId(), spaceship.getName());
        kafkaTemplate.send(TOPIC, message);
    }
}