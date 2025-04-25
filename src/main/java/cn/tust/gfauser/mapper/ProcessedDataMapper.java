package cn.tust.gfauser.mapper;

import cn.tust.gfauser.pojo.ProcessedData;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProcessedDataMapper {
    @Insert("insert into processeddata(name,type,value,date) values(#{name},#{type},#{value},#{date})")
    void add(ProcessedData processedData);
    @Delete("delete from processeddata where id=#{id}")
    void delete(Integer id);
    @Select("select * from processeddata where id=#{id}")
    ProcessedData findById(Integer id);
}
