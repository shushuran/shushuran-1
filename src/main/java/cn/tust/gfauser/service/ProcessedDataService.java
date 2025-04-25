package cn.tust.gfauser.service;

import cn.tust.gfauser.pojo.ProcessedData;

public interface ProcessedDataService {
    void addProcessedData(ProcessedData data);
    void deleteProcessedData(Integer id);
    ProcessedData findProcessedDataById(Integer id);
}
