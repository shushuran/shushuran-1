package cn.tust.gfauser.controller;

import cn.tust.gfauser.pojo.ProcessedData;
import cn.tust.gfauser.pojo.Result;
import cn.tust.gfauser.service.ProcessedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/processeddata")
@Validated
public class ProcessedDataController {
    @Autowired
    private ProcessedDataService processedDataService;

    @PostMapping("/add")
    public Result addProcessedData(@RequestBody @Validated ProcessedData data) {
        processedDataService.addProcessedData(data);
        return Result.success();

    }

    @RequestMapping("/delete")
    public Result deleteProcessedData(@RequestParam Integer id) {
        processedDataService.deleteProcessedData(id);
        return Result.success();
    }

    @RequestMapping("/findbyid")
    public Result findProcessedDataById(@RequestParam Integer id) {

        return Result.success(processedDataService.findProcessedDataById(id));
    }
}
