package com.portizdev.spaceships.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.portizdev.spaceships.service.SpaceshipService.getSpaceshipById(..)) && args(id)")
    public void logIfNegativeId(Long id) {
        if (id < 0) {
            System.out.println("Warning: Attempted to fetch spaceship with negative id: " + id);
        }
    }
}
