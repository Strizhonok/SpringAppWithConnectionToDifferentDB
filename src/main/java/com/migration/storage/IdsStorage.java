package com.migration.storage;

import java.util.HashMap;
import java.util.Map;

public class IdsStorage {

    public static final Map<Long, Long> mySqlToPostgresUserId = new HashMap<>();

    public static final Map<Long, Long> mySqlToPostgresProductId = new HashMap<>();

    public static final Map<String, Long> mySqlOrderNumToPostgresOrderId = new HashMap<>();

}
