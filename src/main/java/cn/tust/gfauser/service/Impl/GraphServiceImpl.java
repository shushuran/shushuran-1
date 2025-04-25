// 文件路径: E:\GFAuser\src\main\java\cn\tust\gfauser\service\impl\GraphServiceImpl.java
package cn.tust.gfauser.service.Impl;

import cn.tust.gfauser.mapper.GraphMapper;
import cn.tust.gfauser.pojo.Graph;
import cn.tust.gfauser.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphServiceImpl implements GraphService {
    @Autowired
    private GraphMapper graphMapper;

    @Override
    public List<Graph> getGraph() {
        return graphMapper.select();
    }
}
