package ru.clevertec.ecl.integration.repository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TagRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void checkFindByNameShouldReturnOptionalTag() {
        Long expectedId = 1L;
        String name = "beauty";

        Optional<Tag> actualTag = tagRepository.findByName(name);

        assertThat(actualTag).isPresent();
        assertThat(actualTag.get().getId()).isEqualTo(expectedId);
    }

    @Test
    void checkFindByNameShouldReturnEmptyOptionalTag() {
        String name = "spa";

        Optional<Tag> actualTag = tagRepository.findByName(name);

        assertThat(actualTag).isNotPresent();
    }

    @Test
    void checkFindMostWidelyUsedByUserWithHighestAmountOrdersShouldReturnOptionalTag() {
        Long expectedId = 2L;

        Optional<Tag> actualTag = tagRepository.findMostWidelyUsedByUserWithHighestAmountOrders();

        assertThat(actualTag).isPresent();
        assertThat(actualTag.get().getId()).isEqualTo(expectedId);
    }
}
