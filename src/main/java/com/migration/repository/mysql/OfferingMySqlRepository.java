package com.migration.repository.mysql;

import com.migration.domain.entity.mysql.MySqlOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferingMySqlRepository extends JpaRepository<MySqlOffering, Long> {

}
