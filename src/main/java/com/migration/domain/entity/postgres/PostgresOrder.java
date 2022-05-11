package com.migration.domain.entity.postgres;

import com.migration.domain.enums.SchemaNames;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Represents PostgresOrder table
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = SchemaNames.SALES, name = "order")
public class PostgresOrder {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NonNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @NonNull
    @Column(name = "num")
    private String num;

    @NotNull
    @NonNull
    @Column(name = "date")
    private LocalDate date;

}
