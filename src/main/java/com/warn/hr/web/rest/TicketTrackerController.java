package com.warn.hr.web.rest;

import com.warn.hr.business.TaskService;
import com.warn.hr.persistence.entity.task.Task;
import com.warn.hr.persistence.entity.task.TicketTask;
import com.warn.hr.web.rest.common.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class TicketTrackerController extends AbstractController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/hr/api/v1/task/list")
    public @ResponseBody ResponseEntity<List<Task>> list(Task task) {
        return list(() -> taskService.listOpenTasks(task, null));
    }

}
