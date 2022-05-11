package com.migration.repository.postgres;

import com.migration.domain.entity.postgres.PostgresUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostgresRepository extends JpaRepository<PostgresUser, Long> {

}
