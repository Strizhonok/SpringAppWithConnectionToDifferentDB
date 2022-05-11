package com.migration.service;

import com.migration.domain.entity.mysql.MySqlUser;
import com.migration.domain.entity.postgres.PostgresUser;
import com.migration.repository.mysql.UserMySqlRepository;
import com.migration.repository.postgres.UserPostgresRepository;
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
public class UserServiceImpl implements UserService {

    private final UserPostgresRepository userPostgresRepository;

    private final UserMySqlRepository userMySqlRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void migrateUsers() {
        final List<MySqlUser> usersFromMySql = userMySqlRepository.findAll();

        final Map<Long, PostgresUser> mySqlUserIdToPostgresUser = new HashMap<>(usersFromMySql.size());
        final List<PostgresUser> usersForPostgres = toListOfPostgresUser(usersFromMySql, mySqlUserIdToPostgresUser);

        userPostgresRepository.saveAll(usersForPostgres);

        //save ids for next tables
        IdsStorage.mySqlToPostgresUserId.clear();
        mySqlUserIdToPostgresUser.forEach((id, postgresUser) -> IdsStorage.mySqlToPostgresUserId.put(id, postgresUser.getId()));
    }

    private List<PostgresUser> toListOfPostgresUser(List<MySqlUser> users,
        Map<Long, PostgresUser> mySqlUserIdToPostgresUser
    ) {
        final List<PostgresUser> postgresUsers = new ArrayList<>(users.size());

        for (MySqlUser user : users) {
            final PostgresUser postgresUser = new PostgresUser();
            postgresUser.setLogin(user.getLogin());
            postgresUser.setName(user.getName());
            postgresUser.setSecondName(user.getSecondName());
            postgresUser.setPassword(user.getPassword());

            postgresUsers.add(postgresUser);
            mySqlUserIdToPostgresUser.put(user.getId(), postgresUser);
        }
        return postgresUsers;
    }

}
