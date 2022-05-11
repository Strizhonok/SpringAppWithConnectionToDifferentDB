package com.migration.repository.mysql;

import com.migration.domain.entity.mysql.MySqlOrderItem;
import com.migration.domain.projection.mysql.MySqlOrderItemView;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemMySqlRepository extends JpaRepository<MySqlOrderItem, Long> {

    @Query("Select oi From MySqlOrderItem oi ")
    Set<MySqlOrderItemView> findAllViews();

}
