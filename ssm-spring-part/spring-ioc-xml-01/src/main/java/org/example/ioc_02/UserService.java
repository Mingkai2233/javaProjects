package org.example.ioc_02;

class UserDao{}

public class UserService {
    private UserDao userDao;
    private int userId;

    public UserService() {}
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    public UserService(UserDao userDao, int userId) {
        this.userDao = userDao;
        this.userId = userId;
    }
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
