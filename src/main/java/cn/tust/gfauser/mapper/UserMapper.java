package cn.tust.gfauser.mapper;

import cn.tust.gfauser.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    // 根据账户查询用户
    @Select("select * from map_user where user_name = #{user_name}")
    User findUserByName(String user_name);
    //添加用户
    @Insert("insert into map_user(user_name,user_email,user_passwd,status) values(#{user_name},#{user_email},#{user_passwd},#{status})")
    void addUser(String user_name, String user_email, String user_passwd,Integer status);  //删除用户
    @Delete("delete from map_user where id = #{id}")
    void deleteUser(Integer id);

    //修改密码
    @Insert("update map_user set user_passwd = #{newPasswd} where id = #{id}")
    void updatePassword(String newPasswd,Integer id);
    @Update("update map_user set user_name = #{userName},user_email = #{userEmail} where id = #{id}")
    void updateUser(User user);
    @Select("select status from map_user where id = #{id}")
    Integer findStatusById(Integer id);
}
