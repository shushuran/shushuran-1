package cn.tust.gfauser.mapper;

import cn.tust.gfauser.pojo.GisData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GisDataMapper {
    @Insert("insert into gis_data(data_id, data_name, data_date, data_url) values(#{data_id}, #{data_name}, #{data_date}, #{data_url})")
    void insertGisData(GisData gisData);
    @Select("select * from gis_data")
    List<GisData> selectGisData();
    @Select("select * from gis_data where data_id = #{data_id}")
    GisData selectGisDataById(String data_id);
    @Select("select * from gis_data where data_name = #{data_name}")
    GisData selectGisDataByName(String data_name);
    @Delete("delete from gis_data where data_id = #{data_id}")
    void deleteGisDataById(String data_id);
    @Update({
            "<script>",
            "update gis_data",
            "<set>",
            "<if test='data_name != null'>data_name = #{data_name},</if>",
            "<if test='data_date != null'>data_date = #{data_date},</if>",
            "<if test='data_url != null'>data_url = #{data_url},</if>",
            "</set>",
            "where data_id = #{data_id}",
            "</script>"
    })
    void updateGisData(GisData gisData);


}
