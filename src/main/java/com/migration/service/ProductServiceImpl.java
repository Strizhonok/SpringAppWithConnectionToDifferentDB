package com.migration.service;

import com.migration.domain.entity.mysql.MySqlOffering;
import com.migration.domain.entity.postgres.PostgresProduct;
import com.migration.repository.mysql.OfferingMySqlRepository;
import com.migration.repository.postgres.ProductPostgresRepository;
import com.migration.storage.IdsStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductPostgresRepository productPostgresRepository;

    private final OfferingMySqlRepository offeringMySqlRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void migrateProducts() {
        final List<MySqlOffering> mySqlOfferings = offeringMySqlRepository.findAll();

        final Map<Long, PostgresProduct> mySqlOfferingIdToPostgresProduct = new HashMap<>(mySqlOfferings.size());
        final List<PostgresProduct> postgresProducts = toListOfPostgresProduct(mySqlOfferings,
            mySqlOfferingIdToPostgresProduct);

        productPostgresRepository.saveAll(postgresProducts);

        //save ids for next tables
        IdsStorage.mySqlToPostgresProductId.clear();
        mySqlOfferingIdToPostgresProduct.forEach((id, postgresProduct) -> IdsStorage.mySqlToPostgresProductId.put(id,
            postgresProduct.getId()));
    }

    private List<PostgresProduct> toListOfPostgresProduct(List<MySqlOffering> mySqlOfferings,
        Map<Long, PostgresProduct> mySqlOfferingIdToPostgresProduct
    ) {
        final List<PostgresProduct> postgresProducts = new ArrayList<>(mySqlOfferings.size());

        for (MySqlOffering offering : mySqlOfferings) {
            final PostgresProduct postgresProduct = new PostgresProduct();
            postgresProduct.setName(offering.getName());
            postgresProduct.setPrice(offering.getPrice());
            postgresProduct.setCount(offering.getCount());

            postgresProducts.add(postgresProduct);
            mySqlOfferingIdToPostgresProduct.put(offering.getId(), postgresProduct);
        }

        return postgresProducts;
    }

}
