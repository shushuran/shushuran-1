package cn.tust.gfauser.service.Impl;

import cn.tust.gfauser.mapper.UserMapper;
import cn.tust.gfauser.pojo.User;
import cn.tust.gfauser.service.UserService;
import cn.tust.gfauser.utils.Md5Util;
import cn.tust.gfauser.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper usermapper;
    @Override
    public User findUserById(Integer id) {
        return null;
    }

    @Override
    public User findUserByName(String user_name) {
        return usermapper.findUserByName(user_name);
    }

    @Override
    public User findUserByEmail(String user_email) {
        return null;
    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public void updatePassword(String newPasswd) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        usermapper.updatePassword(Md5Util.getMD5String(newPasswd),id);
    }

    @Override
    public void updateUser(User user) {
        usermapper.updateUser(user);


    }

    @Override
    public void login(String user_name, String user_passwd) {

    }

    @Override
    public void register(String user_name, String user_email, String user_passwd,Integer status) {
        String md5PwdStr = Md5Util.getMD5String(user_passwd);
        usermapper.addUser(user_name,user_email,md5PwdStr,status);
    }

    @Override
    public Integer findStatusById(Integer id) {
        return usermapper.findStatusById(id);
    }
}
