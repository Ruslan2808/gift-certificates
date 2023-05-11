package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.clevertec.ecl.entity.Order;

/**
 * Interface for performing operations with {@link Order} in the database
 *
 * @author Ruslan Kantsevitch
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
