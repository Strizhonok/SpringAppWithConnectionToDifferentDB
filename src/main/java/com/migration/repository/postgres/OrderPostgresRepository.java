package com.migration.repository.postgres;

import com.migration.domain.entity.postgres.PostgresOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPostgresRepository extends JpaRepository<PostgresOrder, Long> {

}
