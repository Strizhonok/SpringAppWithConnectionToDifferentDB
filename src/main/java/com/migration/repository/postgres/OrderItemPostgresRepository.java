package com.migration.repository.postgres;

import com.migration.domain.entity.postgres.PostgresOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemPostgresRepository extends JpaRepository<PostgresOrderItem, Long> {

}
