package com.migration.service;

import com.migration.domain.entity.mysql.MySqlOrderItem;
import com.migration.domain.entity.postgres.PostgresOrderItem;
import com.migration.repository.mysql.OrderItemMySqlRepository;
import com.migration.repository.postgres.OrderItemPostgresRepository;
import com.migration.storage.IdsStorage;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemMySqlRepository orderItemMySqlRepository;

    private final OrderItemPostgresRepository orderItemPostgresRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void migrateOrderItems() {
        final List<MySqlOrderItem> mySqlOrderItems = orderItemMySqlRepository.findAll();
        final List<PostgresOrderItem> postgresOrderItems = toListOfPostgresOrderItem(mySqlOrderItems);

        orderItemPostgresRepository.saveAll(postgresOrderItems);
    }

    private List<PostgresOrderItem> toListOfPostgresOrderItem(List<MySqlOrderItem> orderItems) {
        final List<PostgresOrderItem> postgresOrderItems = new ArrayList<>(orderItems.size());

        for (MySqlOrderItem orderItem : orderItems) {
            final PostgresOrderItem postgresOrderItem = new PostgresOrderItem();
            postgresOrderItem.setCount(orderItem.getCount());
            postgresOrderItem.setPrice(orderItem.getPrice());
            postgresOrderItem.setProductId(IdsStorage.mySqlToPostgresProductId.get(orderItem.getOfferingId()));
            postgresOrderItem.setOrderId(IdsStorage.mySqlOrderNumToPostgresOrderId.get(orderItem.getOrderNum()));

            postgresOrderItems.add(postgresOrderItem);
        }
        return postgresOrderItems;
    }

}
