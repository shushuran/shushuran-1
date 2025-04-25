package cn.tust.gfauser.service;

import cn.tust.gfauser.pojo.User;

public interface UserService {
    User findUserById(Integer id);
    User findUserByName(String user_name);
    User findUserByEmail(String user_email);
    void addUser(User user);
    void deleteUser(Integer id);
    void updatePassword(String newPasswd);
    void updateUser(User user);
    void login(String user_name,String user_passwd);
    void register(String user_name,String user_email,String user_passwd,Integer status);

    Integer findStatusById(Integer id);
}
