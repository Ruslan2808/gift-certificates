package ru.clevertec.ecl.integration;

import org.junit.jupiter.api.BeforeAll;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import org.testcontainers.containers.PostgreSQLContainer;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:db/test_data.sql")
public abstract class BaseIntegrationTest {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13.3");

    @BeforeAll
    static void init() {
        container.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
