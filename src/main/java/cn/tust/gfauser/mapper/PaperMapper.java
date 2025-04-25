package cn.tust.gfauser.mapper;

import cn.tust.gfauser.pojo.Paper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaperMapper {

    @Insert("INSERT INTO papers (title, author, text, Pushdate, Updatedate, type) " +
            "VALUES (#{title}, #{author}, #{text}, #{Pushdate}, #{Updatedate}, #{type})")
    void addPaper(Paper paper);

    @Delete("DELETE FROM papers WHERE id = #{id}")
    void deletePaper(Integer id);

    @Select("SELECT * FROM papers WHERE id = #{id}")
    Paper findPaperById(Integer id);

    @Select("SELECT * FROM papers WHERE title LIKE CONCAT('%', #{title}, '%')")
    List<Paper> findPaperByTitle(String title);

    @Update("UPDATE papers SET title = #{title}, text = #{text}, " +
            " Updatedate = #{Updatedate}, type = #{type} WHERE id = #{id}")
    void updatePaper(Paper paper);

    @Select("SELECT * FROM papers ORDER BY pushdate DESC")
    List<Paper> listPapers();

    @Select("""
        SELECT p.*
        FROM papers p
        INNER JOIN (
            SELECT type, MAX(pushdate) AS max_pushdate
            FROM papers
            WHERE type IN ('天气预警', '病虫害防治', '市场行情', '种植技术')
            GROUP BY type
        ) latest ON p.type = latest.type AND p.pushdate = latest.max_pushdate
    """)
    List<Paper> findLatestPapers();
}