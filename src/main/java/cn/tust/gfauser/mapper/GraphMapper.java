// 文件路径: E:\GFAuser\src\main\java\cn\tust\gfauser\mapper\GraphMapper.java
package cn.tust.gfauser.mapper;

import cn.tust.gfauser.pojo.Graph;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GraphMapper {
    @Select("SELECT * FROM map_graph")
    List<Graph> select();
}
