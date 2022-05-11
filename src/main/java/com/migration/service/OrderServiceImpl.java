package com.migration.service;

import com.migration.domain.entity.postgres.PostgresOrder;
import com.migration.domain.projection.mysql.MySqlOrderItemView;
import com.migration.repository.mysql.OrderItemMySqlRepository;
import com.migration.repository.postgres.OrderPostgresRepository;
import com.migration.storage.IdsStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderPostgresRepository orderPostgresRepository;

    private final OrderItemMySqlRepository orderItemMySqlRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void migrateOrders() {
        final Set<MySqlOrderItemView> orderItemViews = orderItemMySqlRepository.findAllViews();

        final Map<String, PostgresOrder> mySqlOrderNumToPostgresOrder = new HashMap<>(orderItemViews.size());
        final List<PostgresOrder> ordersForPostgres = toListOfPostgresOrders(orderItemViews,
            mySqlOrderNumToPostgresOrder);

        orderPostgresRepository.saveAll(ordersForPostgres);

        //save ids for next tables
        IdsStorage.mySqlOrderNumToPostgresOrderId.clear();
        mySqlOrderNumToPostgresOrder.forEach(
            (orderNum, postgresOrder) -> IdsStorage.mySqlOrderNumToPostgresOrderId.put(orderNum,
                postgresOrder.getId()));
    }

    private List<PostgresOrder> toListOfPostgresOrders(Set<MySqlOrderItemView> orderItemViews,
        Map<String, PostgresOrder> mySqlOrderNumToPostgresOrder
    ) {
        final List<PostgresOrder> postgresOrders = new ArrayList<>(orderItemViews.size());

        for(MySqlOrderItemView orderItemView : orderItemViews) {
            if (mySqlOrderNumToPostgresOrder.containsKey(orderItemView.getOrderNum())) {
                continue;
            }

            final PostgresOrder postgresOrder = new PostgresOrder();
            postgresOrder.setNum(orderItemView.getOrderNum());
            postgresOrder.setDate(orderItemView.getDate());
            postgresOrder.setUserId(IdsStorage.mySqlToPostgresUserId.get(orderItemView.getUserId()));

            postgresOrders.add(postgresOrder);
            mySqlOrderNumToPostgresOrder.put(orderItemView.getOrderNum(), postgresOrder);
        }

        return postgresOrders;
    }

}
