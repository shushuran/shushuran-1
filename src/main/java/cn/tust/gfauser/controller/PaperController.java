package cn.tust.gfauser.controller;

import cn.tust.gfauser.pojo.PageBean;
import cn.tust.gfauser.pojo.Paper;
import cn.tust.gfauser.pojo.Result;
import cn.tust.gfauser.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/paper")
@Validated
public class PaperController {

    @Autowired
    private PaperService paperService;

    @PostMapping("/add")
    public Result add(@RequestBody @Validated Paper paper) {
        paperService.addPaper(paper); // 调用Service层添加论文
        return Result.success(); // 返回成功结果
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        paperService.deletePaper(id); // 调用Service层删除论文
        return Result.success(); // 返回成功结果
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated Paper paper) {
        paperService.updatePaper(paper); // 调用Service层更新论文
        return Result.success(); // 返回成功结果
    }

    @GetMapping("/findbyid")
    public Result findById(@RequestParam Integer id) {
        Paper paper = paperService.findPaperById(id); // 调用Service层根据ID查询论文
        return Result.success(paper); // 返回查询结果
    }

    @GetMapping("/findbytitle")
    public Result findByTitle(@RequestParam String title) {
        List<Paper> papers = paperService.findPaperByTitle(title); // 调用Service层根据标题模糊查询论文
        return Result.success(papers); // 返回查询结果
    }

    @GetMapping("/list")
    public Result<PageBean<Paper>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<Paper> pageBean = paperService.listPapers(pageNum, pageSize); // 调用Service层分页查询论文列表
        return Result.success(pageBean); // 返回分页结果
    }

    @GetMapping("/latest-papers")
    public Map<String, Paper> getLatestPapers() {
        return paperService.getLatestPapersByType();
    }
}