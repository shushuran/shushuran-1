package cn.tust.gfauser.controller;


import cn.tust.gfauser.pojo.Result;
import cn.tust.gfauser.pojo.User;
import cn.tust.gfauser.service.UserService;
import cn.tust.gfauser.utils.JwtUtil;
import cn.tust.gfauser.utils.Md5Util;
import cn.tust.gfauser.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @RequestMapping("/list")
    public Result list(){
        return Result.success(userService.findUserById(1));
    }
    @PostMapping("/find")
    public Result find(String user_name){
        User user = userService.findUserByName(user_name);
        return Result.success(user);
    }
    @PostMapping("/login")
    public Result login(@RequestParam String user_name, @RequestParam String user_passwd){
        // 根据用户名查询用户
        User loginUser = userService.findUserByName(user_name);
        // 判断该用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误");
        }

        //判断密码是否正确  loginUser对象中的password是密文
        if (Md5Util.getMD5String(user_passwd).equals(loginUser.getUserPasswd())) {
            //登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUserName());
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String token) {
        // 从请求头中获取token
        if (token == null) {
            return Result.error("无效的Token");
        }
        // 从Redis中删除对应的键
        Boolean deleteResult = redisTemplate.delete(token);

        // 根据删除结果返回不同的响应
        if (deleteResult) {
            // 键被成功删除，表示登出成功
            return Result.success("登出成功");
        } else {
            // 键不存在，可能表示该token从未被存储或已过期
            return Result.error("无效的Token或已登出");
        }
    }
    @PostMapping("/register")
    public Result register(@RequestParam String user_name, @RequestParam String user_email, @RequestParam String user_passwd){
        //查询用户
        System.out.println(user_name);
        User u = userService.findUserByName(user_name);
        int status = 0;
        System.out.println(u);
        if (u == null) {
            //没有占用
            //注册
            userService.register(user_name,user_email,user_passwd,status);
            return Result.success();
        } else {
            //占用
            return Result.error("用户名已被占用");
        }
    }
    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/) {
        //根据用户名查询用户
       /* Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findUserByName(username);
        return Result.success(user);
    }
    @GetMapping("/status")
    public Result<Integer> findstatus(/*@RequestHeader(name = "Authorization") String token*/) {
        //根据用户名查询用户
       /* Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        Integer status = userService.findStatusById(id);
        return Result.success(status);
    }
    @GetMapping("/findusername")
    public Result<String> findusername() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        return Result.success(username);
    }
    @GetMapping("findid")

    public Result<String> findid() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String userid = (String) map.get("id");
        return Result.success(userid);
    }
    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.updateUser(user);
        return Result.success();
    }
    @PostMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params,@RequestHeader("Authorization") String token) {
        //1.校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数");
        }

        //原密码是否正确
        //调用userService根据用户名拿到原密码,再和old_pwd比对
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findUserByName(username);
        if (!loginUser.getUserPasswd().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码填写不正确");
        }

        //newPwd和rePwd是否一样
        if (!rePwd.equals(newPwd)){
            return Result.error("两次填写的新密码不一样");
        }

        //2.调用service完成密码更新
        userService.updatePassword(newPwd);
        //删除redis中对应的token
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }
}
