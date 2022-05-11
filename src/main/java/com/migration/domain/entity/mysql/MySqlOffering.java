package com.migration.domain.entity.mysql;

import com.migration.domain.enums.SchemaNames;
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
 * Represents MySqlOffering table
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = SchemaNames.ORDERS, name = "offering")
public class MySqlOffering {

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @NonNull
    @Column(name = "name")
    private String name;

    @NotNull
    @NonNull
    @Column(name = "price")
    private Long price;

    @NotNull
    @NonNull
    @Column(name = "count")
    private Long count;

}
