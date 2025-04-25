package cn.tust.gfauser.service.Impl;

import cn.tust.gfauser.mapper.ProcessedDataMapper;
import cn.tust.gfauser.pojo.ProcessedData;
import cn.tust.gfauser.service.ProcessedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessedDataServiceImpl implements ProcessedDataService {

    @Autowired
    private ProcessedDataMapper processedDataMapper;
    @Override
    public void addProcessedData(ProcessedData data) {
        processedDataMapper.add(data);
    }

    @Override
    public void deleteProcessedData(Integer id) {
        processedDataMapper.delete(id);
    }

    @Override
    public ProcessedData findProcessedDataById(Integer id) {
        return processedDataMapper.findById(id);
    }
}
