package ru.clevertec.ecl.repository.impl;

import lombok.RequiredArgsConstructor;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

import org.springframework.stereotype.Repository;

import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.List;
import java.util.Optional;

/**
 * An implementation of the {@link TagRepository} interface for performing operations with {@link Tag}
 * {@link SessionFactory} is used to perform operations on tags of type {@link Tag} and manage
 * transactions in the database
 *
 * @author Ruslan Kantsevich
 */
@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory sessionFactory;

    /**
     * Returns a list of all tags of type {@link Tag} in the database
     *
     * @return the list of all tags of type {@link Tag} or an empty list if there are
     * none in the database
     */
    @Override
    public List<Tag> findAll() {
        final String selectAllQuery = "SELECT t FROM Tag t";

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Tag> tags = session.createQuery(selectAllQuery, Tag.class)
                    .list();
            transaction.commit();

            return tags;
        }
    }

    /**
     * Returns a tag of type {@link Optional<Tag>} by id or empty optional tag
     * if not found in the database
     *
     * @param id the tag id
     * @return the tag of type {@link Optional<Tag>} with given id
     */
    @Override
    public Optional<Tag> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Tag tag = session.get(Tag.class, id);
            transaction.commit();

            return Optional.ofNullable(tag);
        }
    }

    /**
     * Returns a tag of type {@link Optional<Tag>} by name or empty optional tag
     * if not found in the database
     *
     * @param name the tag name
     * @return the tag of type {@link Optional<Tag>} with given name
     */
    @Override
    public Optional<Tag> findByName(String name) {
        final String selectByNameQuery = "SELECT t FROM Tag t WHERE name = :name";

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Optional<Tag> optionalTag = session.createQuery(selectByNameQuery, Tag.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
            transaction.commit();

            return optionalTag;
        }
    }

    /**
     * Saves the tag of type {@link Tag} in the database
     *
     * @param tag the tag of type {@link Tag} to save
     * @return the saved tag of type {@link Tag}
     */
    @Override
    public Tag save(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(tag);
            transaction.commit();

            return tag;
        }
    }

    /**
     * Updates the tag of type {@link Tag} in the database
     *
     * @param tag the tag of type {@link Tag} to update
     * @return the updated tag of type {@link Tag}
     */
    @Override
    public Tag update(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Tag updatedTag = session.merge(tag);
            transaction.commit();

            return updatedTag;
        }
    }

    /**
     * Deletes the tag of type {@link Tag} with the given id from the database
     *
     * @param id the tag id to be deleted
     */
    @Override
    public void deleteById(Long id) {
        final String deleteGiftCertificateTagQuery = "DELETE FROM gift_certificate_tag WHERE tag_id = :id";

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            MutationQuery deleteGiftCertificateTag = session.createNativeMutationQuery(deleteGiftCertificateTagQuery);
            deleteGiftCertificateTag.setParameter("id", id);
            deleteGiftCertificateTag.executeUpdate();
            Tag tag = session.get(Tag.class, id);
            session.remove(tag);

            transaction.commit();
        }
    }
}
