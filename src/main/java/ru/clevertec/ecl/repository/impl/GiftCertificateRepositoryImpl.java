package ru.clevertec.ecl.repository.impl;

import lombok.RequiredArgsConstructor;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.stereotype.Repository;

import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;

import java.util.List;
import java.util.Optional;

/**
 * An implementation of the {@link GiftCertificateRepository} interface for performing operations
 * with {@link GiftCertificate}
 * {@link SessionFactory} is used to perform operations on gift certificates of type {@link GiftCertificate}
 * and manage transactions in the database
 *
 * @author Ruslan Kantsevich
 */
@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final SessionFactory sessionFactory;

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} in the database
     *
     * @return the list of all gift certificates of type {@link GiftCertificate} or an empty
     * list if there are none in the database
     */
    @Override
    public List<GiftCertificate> findAll() {
        final String selectAllQuery = "SELECT gc FROM GiftCertificate gc LEFT JOIN FETCH gc.tags";

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<GiftCertificate> giftCertificates = session
                    .createQuery(selectAllQuery, GiftCertificate.class)
                    .list();
            transaction.commit();

            return giftCertificates;
        }
    }

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} with
     * a specific tag name in the database
     *
     * @param tagName the gift certificate tag name
     * @return the list of all gift certificates of type {@link GiftCertificate}
     * or an empty list if there are none in the database
     */
    @Override
    public List<GiftCertificate> findAllByTagName(String tagName) {
        final String selectAllByTagNameQuery =
                "SELECT gc FROM GiftCertificate gc INNER JOIN FETCH gc.tags t WHERE t.name = :name";

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<GiftCertificate> giftCertificates = session
                    .createQuery(selectAllByTagNameQuery, GiftCertificate.class)
                    .setParameter("name", tagName)
                    .list();
            transaction.commit();

            return giftCertificates;
        }
    }

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} containing
     * part of the name or description in the database
     *
     * @param part the part of the name or description
     * @return the list of all gift certificates of type {@link GiftCertificate}
     * or an empty list if there are none in the database
     */
    @Override
    public List<GiftCertificate> findAllByPartNameOrDescription(String part) {
        final String selectAllByPartNameOrDescriptionQuery =
                "SELECT gc FROM GiftCertificate gc LEFT JOIN FETCH gc.tags t WHERE gc.name LIKE :part OR gc.description LIKE :part";

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<GiftCertificate> giftCertificates = session
                    .createQuery(selectAllByPartNameOrDescriptionQuery, GiftCertificate.class)
                    .setParameter("part", "%" + part + "%")
                    .list();
            transaction.commit();

            return giftCertificates;
        }
    }

    /**
     * Returns a list sorted by parameter and order of all gift certificates of type {@link GiftCertificate}
     * in the database
     *
     * @param sortBy the parameter by which gift certificates are sorted
     * @param order  the sort order of gift certificates by the given parameter
     * @return the list of all gift certificates of type {@link GiftCertificate}
     * or an empty list if there are none in the database
     */
    @Override
    public List<GiftCertificate> findAllSortByParam(String sortBy, String order) {
        StringBuilder selectAllSortByParamQuery =
                new StringBuilder("SELECT gc FROM GiftCertificate gc LEFT JOIN FETCH gc.tags ORDER BY ");

        selectAllSortByParamQuery.append(String.format("gc.%s ", sortBy));
        if ("desc".equalsIgnoreCase(order)) {
            selectAllSortByParamQuery.append("DESC");
        } else {
            selectAllSortByParamQuery.append("ASC");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<GiftCertificate> giftCertificates = session
                    .createQuery(selectAllSortByParamQuery.toString(), GiftCertificate.class)
                    .list();
            transaction.commit();

            return giftCertificates;
        }
    }

    /**
     * Returns a gift certificate of type {@link Optional<GiftCertificate>} by id or
     * empty optional gift certificate if not found in the database
     *
     * @param id the gift certificate id
     * @return the gift certificate of type {@link Optional<GiftCertificate>} with given id
     */
    @Override
    public Optional<GiftCertificate> findById(Long id) {
        final String selectByIdQuery = "SELECT gc FROM GiftCertificate gc LEFT JOIN FETCH gc.tags WHERE gc.id = :id";

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Optional<GiftCertificate> optionalGiftCertificate = session
                    .createQuery(selectByIdQuery, GiftCertificate.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
            transaction.commit();

            return optionalGiftCertificate;
        }
    }

    /**
     * Saves the gift certificate of type {@link GiftCertificate} in the database
     *
     * @param giftCertificate the gift certificate of type {@link GiftCertificate} to save
     * @return the saved gift certificate of type {@link GiftCertificate}
     */
    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            giftCertificate.setTags(updateTags(session, giftCertificate));
            session.persist(giftCertificate);
            transaction.commit();

            return giftCertificate;
        }
    }

    /**
     * Updates the gift certificate of type {@link GiftCertificate} in the database
     *
     * @param giftCertificate the gift certificate of type {@link GiftCertificate} to update
     * @return the updated gift certificate of type {@link GiftCertificate}
     */
    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            giftCertificate.setTags(updateTags(session, giftCertificate));
            GiftCertificate updatedGiftCertificate = session.merge(giftCertificate);
            transaction.commit();

            return updatedGiftCertificate;
        }
    }

    /**
     * Deletes the gift certificate of type {@link GiftCertificate} with the given id from the database
     *
     * @param id the gift certificate id to be deleted
     */
    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            GiftCertificate giftCertificate = session.get(GiftCertificate.class, id);
            session.remove(giftCertificate);

            transaction.commit();
        }
    }

    /**
     * Update gift certificates of type {@link GiftCertificate} tags. If the tag of type {@link Tag}
     * exists in the database, it will be bound to the gift certificate. If the tag does not exist
     * in the database, it will be created and linked to the certificate
     *
     * @param session         the session within which the gift certificate tags of type {@link Tag} are updated
     * @param giftCertificate gift certificate of type {@link GiftCertificate} whose tags need to be updated
     */
    private List<Tag> updateTags(Session session, GiftCertificate giftCertificate) {
        final String selectByNameQuery = "SELECT t FROM Tag t WHERE name = :name";

        return giftCertificate.getTags().stream()
                .map(tag -> session.createQuery(selectByNameQuery, Tag.class)
                        .setParameter("name", tag.getName())
                        .uniqueResultOptional()
                        .orElse(tag))
                .toList();
    }
}
