package com.migration.repository.mysql;

import com.migration.domain.entity.mysql.MySqlUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMySqlRepository extends JpaRepository<MySqlUser, Long> {

}
