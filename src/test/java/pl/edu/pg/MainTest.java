package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void getHelloShouldReturnHello() {
        assertEquals("Hello world!", new Main().getHello());
    }
}