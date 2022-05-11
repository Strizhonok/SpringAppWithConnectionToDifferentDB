package com.migration.domain.projection.mysql;

import java.time.LocalDate;

/**
 * Order Item Projection
 */
public interface MySqlOrderItemView {

    Long getUserId();

    String getOrderNum();

    LocalDate getDate();

}
