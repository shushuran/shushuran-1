package cn.tust.gfauser.controller;

import cn.tust.gfauser.pojo.Graph;
import cn.tust.gfauser.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/graphs")
public class GraphController {
    @Autowired
    private GraphService graphService;

    @GetMapping("/list")
    public List<Graph> getGraph() {
        return graphService.getGraph();
    }
}
