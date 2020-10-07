package com.masmovil.masbarrings.barrings.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainerExample {

  @Container
  static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer()
      .withDatabaseName("test")
      .withUsername("duke")
      .withPassword("s3cret")
      .withReuse(true);

  @Before
  public void before() throws InterruptedException {

    postgreSQLContainer.start();
  }

  @Test
  @DisplayName("Test connection")
  public void checkConnectionContainer() {
    System.out.println("BARRING - Database - TEST : " + postgreSQLContainer);
    System.out.println("IMAGE DOCKER -> " + postgreSQLContainer.getDockerImageName());
    assertEquals(true,postgreSQLContainer.isRunning());
  }
}