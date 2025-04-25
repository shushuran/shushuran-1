package cn.tust.gfauser.service;

import cn.tust.gfauser.pojo.PageBean;
import cn.tust.gfauser.pojo.Paper;

import java.util.List;
import java.util.Map;
public interface PaperService {
    void addPaper(Paper paper); // 添加论文
    void deletePaper(Integer id); // 删除论文
    Paper findPaperById(Integer id); // 根据ID查询论文
    List<Paper> findPaperByTitle(String title); // 根据标题模糊查询论文
    void updatePaper(Paper paper); // 更新论文
    PageBean<Paper> listPapers(Integer pageNum, Integer pageSize); // 分页查询论文列表

    Map<String,Paper> getLatestPapersByType();
}