package cn.tust.gfauser.service.Impl;

import cn.tust.gfauser.mapper.PaperMapper;
import cn.tust.gfauser.pojo.PageBean;
import cn.tust.gfauser.pojo.Paper;
import cn.tust.gfauser.service.PaperService;
import cn.tust.gfauser.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperMapper paperMapper;


    @Override
    public void addPaper(Paper paper) {
            //补充属性值
            paper.setPushdate(LocalDateTime.now());
            paper.setUpdatedate(LocalDateTime.now());

            Map<String,Object> map = ThreadLocalUtil.get();
            String username = (String) map.get("username");
            paper.setAuthor(username);
            paperMapper.addPaper(paper); // 调用Mapper插入论文
    }

    @Override
    public void deletePaper(Integer id) {
        paperMapper.deletePaper(id); // 调用Mapper删除论文
    }

    @Override
    public Paper findPaperById(Integer id) {
        return paperMapper.findPaperById(id); // 调用Mapper根据ID查询论文
    }

    @Override
    public List<Paper> findPaperByTitle(String title) {
        return paperMapper.findPaperByTitle(title); // 调用Mapper根据标题模糊查询论文
    }

    @Override
    public void updatePaper(Paper paper) {
        paper.setUpdatedate(LocalDateTime.now());
        paperMapper.updatePaper(paper); // 调用Mapper更新论文
    }

    @Override
    public PageBean<Paper> listPapers(Integer pageNum, Integer pageSize) {
        // 创建分页对象
        PageBean<Paper> pageBean = new PageBean<>();

        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);

        // 调用Mapper查询论文列表
        List<Paper> paperList = paperMapper.listPapers();

        // 将查询结果转换为Page对象
        Page<Paper> page = (Page<Paper>) paperList;

        // 设置分页数据
        pageBean.setTotal(page.getTotal()); // 总记录数
        pageBean.setItems(page.getResult()); // 当前页数据

        return pageBean;
    }
    public Map<String, Paper> getLatestPapersByType() {
        List<Paper> papers = paperMapper.findLatestPapers();
        Map<String, Paper> result = new HashMap<>();
        for (Paper paper : papers) {
            result.put(paper.getType(), paper);
        }
        return result;
    }
}