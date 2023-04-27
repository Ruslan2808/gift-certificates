package ru.clevertec.ecl.repository.impl;

import org.assertj.core.api.Assertions;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ru.clevertec.ecl.config.DataBaseConfig;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.util.tag.TagTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringJUnitConfig(DataBaseConfig.class)
class TagRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository = new TagRepositoryImpl(sessionFactory);
    }

    @Nested
    class TagRepositoryImplFindAllTest {

        @Test
        void checkFindAllShouldReturnNotEmptyGiftCertificates() {
            List<Tag> tags = tagRepository.findAll();

            assertThat(tags).isNotEmpty();
        }
    }

    @Nested
    class TagRepositoryImplFindByIdTest {
        @Test
        void checkFindByIdShouldReturnNotEmptyOptionalTag() {
            Long expectedId = 1L;
            String expectedName = "health";

            Optional<Tag> actualOptionalTag = tagRepository.findById(expectedId);

            assertThat(actualOptionalTag).isPresent();
            assertAll(() -> {
                assertThat(actualOptionalTag.get().getId()).isEqualTo(expectedId);
                assertThat(actualOptionalTag.get().getName()).isEqualTo(expectedName);
            });
        }

        @Test
        void checkFindByIdShouldReturnEmptyOptionalTag() {
            Long id = 15L;

            Optional<Tag> actualOptionalTag = tagRepository.findById(id);

            assertThat(actualOptionalTag).isNotPresent();
        }
    }

    @Nested
    class TagRepositoryImplFindByNameTest {

        @Test
        void checkFindByNameShouldReturnNotEmptyOptionalTag() {
            Long expectedId = 1L;
            String expectedName = "health";

            Optional<Tag> actualOptionalTag = tagRepository.findByName(expectedName);

            assertThat(actualOptionalTag).isPresent();
            assertAll(() -> {
                assertThat(actualOptionalTag.get().getId()).isEqualTo(expectedId);
                assertThat(actualOptionalTag.get().getName()).isEqualTo(expectedName);
            });
        }

        @Test
        void checkFindByNameShouldReturnEmptyOptionalTag() {
            String name = "beautiful time";

            Optional<Tag> actualOptionalTag = tagRepository.findByName(name);

            assertThat(actualOptionalTag).isNotPresent();
        }
    }

    @Nested
    class TagRepositoryImplSaveTest {

        @Test
        void checkSaveShouldReturnTag() {
            String expectedName = "comfort";
            Tag saveTag = TagTestBuilder.tag()
                    .withId(null)
                    .withName("comfort")
                    .build();

            Tag actualTag = tagRepository.save(saveTag);

            assertThat(actualTag.getName()).isEqualTo(expectedName);
        }

        @Test
        void checkSaveShouldReturnThrowsConstraintViolationException() {
            Tag tag = TagTestBuilder.tag()
                    .withId(null)
                    .withName("health")
                    .build();

            assertThatThrownBy(() -> tagRepository.save(tag))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    class TagRepositoryImplUpdateTest {

        @Test
        void checkUpdateShouldReturnTag() {
            Tag tag = TagTestBuilder.tag()
                    .withId(5L)
                    .withName("read")
                    .build();

            Tag actualTag = tagRepository.update(tag);

            assertThat(actualTag.getName()).isEqualTo(tag.getName());
        }

        @Test
        void checkUpdateShouldReturnThrowsConstraintViolationException() {
            Tag tag = TagTestBuilder.tag()
                    .withId(2L)
                    .withName("health")
                    .build();

            assertThatThrownBy(() -> tagRepository.update(tag))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    class TagRepositoryImplDeleteByIdTest {

        @Test
        void checkDeleteByIdShouldReturnEmptyOptionalTag() {
            Long id = 5L;

            tagRepository.deleteById(id);
            Optional<Tag> actualOptionalTag = tagRepository.findById(id);

            assertThat(actualOptionalTag).isNotPresent();
        }

        @Test
        void checkDeleteByIdShouldThrowsIllegalArgumentException() {
            Long id = 10L;

            assertThatThrownBy(() -> tagRepository.deleteById(id))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}