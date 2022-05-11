package com.migration.domain.entity.mysql;

import com.migration.domain.enums.SchemaNames;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Represents MySqlOrderItem table
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = SchemaNames.ORDERS, name = "order_item")
public class MySqlOrderItem {

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @NonNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @NonNull
    @Column(name = "offering_id")
    private Long offeringId;

    @NotNull
    @NonNull
    @Column(name = "order_num")
    private String orderNum;

    @NotNull
    @NonNull
    @Column(name = "date")
    private LocalDate date;

    @NotNull
    @NonNull
    @Column(name = "price")
    private Long price;

    @NotNull
    @NonNull
    @Column(name = "count")
    private Long count;

    @NotNull
    @NonNull
    @Column(name = "sum")
    private Long sum;

}
