package dao.impl;

import dao.UserDAO;
import domain.User;

public class UserDAOImpl extends BaseDAO<User> implements UserDAO {

    @Override
    public User getUser(String username) {
        String sql = "SELECT userId, username, accountId " +
                "FROM userinfo WHERE username = ?";
        return query(sql, username);
    }

}
