package com.migration.repository.postgres;

import com.migration.domain.entity.postgres.PostgresProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPostgresRepository extends JpaRepository<PostgresProduct, Long> {

}
