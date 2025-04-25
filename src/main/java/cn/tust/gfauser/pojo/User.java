package cn.tust.gfauser.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class User {
    private Integer id; // 用户ID
    @NotEmpty
    private String userName; // 用户名
    private String userEmail; // 用户邮箱
    @JsonIgnore
    private String userPasswd; // 用户密码（忽略JSON序列化）
    private Integer status = 0; // 新增属性，默认值为1
}