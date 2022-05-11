package com.migration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The implementation of {@link MigrationService}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MigrationServiceImpl implements MigrationService {

    private final UserService userService;

    private final ProductService productService;

    private final OrderService orderService;

    private final OrderItemService orderItemService;

    private final Logger logger = LoggerFactory.getLogger(MigrationServiceImpl.class);

    @Override
    public void migrateAll() {
        logger.debug("start migration");
        userService.migrateUsers();
        productService.migrateProducts();
        orderService.migrateOrders();
        orderItemService.migrateOrderItems();
        logger.debug("migration is completed");
    }

}
