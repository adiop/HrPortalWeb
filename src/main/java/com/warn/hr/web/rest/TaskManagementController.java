package com.warn.hr.web.rest;

import com.warn.hr.business.ActiveDirectoryService;
import com.warn.hr.business.TaskService;
import com.warn.hr.business.TicketService;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.hr.UserAccount;
import com.warn.hr.persistence.entity.task.Task;
import com.warn.hr.web.rest.common.AbstractController;
import javaslang.API;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static javaslang.API.$;
import static javaslang.API.Case;

@Slf4j
@RestController
public class TaskManagementController extends AbstractController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ActiveDirectoryService activeDirectoryService;

    // Task operations
    @GetMapping(value = {"/hr/api/v1/task/{id}", "/hr/api/v1/task/{id}/{isListAll}"})
    public @ResponseBody ResponseEntity<Task> findById(@PathVariable("id") Long id,  @PathVariable(value = "isListAll", required = false) boolean isListAll, HttpServletRequest request) {

        ResponseEntity<Task> responseEntity = null;

        if(isListAll) {
            responseEntity = find(() -> taskService.findById(id));
        } else {
            responseEntity = find(() -> taskService.findById(id, getStaff(request).getId()));
        }

        return responseEntity;

    }

    @GetMapping(value = {"/hr/api/v1/task/list/open", "/hr/api/v1/task/list/open/{isListAll}"})
    public @ResponseBody ResponseEntity<List<Task>> listOpenTasks(Task task,
                                                                  @PathVariable(value = "isListAll", required = false) boolean isListAll,
                                                                  HttpServletRequest request) {

        ResponseEntity<List<Task>> responseEntity = null;

        if(isListAll) {
            responseEntity = list(() -> taskService.listOpenTasks(task));
        } else {
            responseEntity = list(() -> taskService.listOpenTasks(task, getStaff(request).getId()));
        }

        return responseEntity;

    }

    @GetMapping(value = {"/hr/api/v1/task/list/closed", "/hr/api/v1/task/list/closed/{isListAll}"})
    public @ResponseBody ResponseEntity<List<Task>> listClosedTasks(Task task,
                                                                    @PathVariable(value = "isListAll", required = false) Boolean isListAll,
                                                                    HttpServletRequest request) {

        ResponseEntity<List<Task>> responseEntity = null;

        if(isListAll) {
            responseEntity = list(() -> taskService.listClosedTasks(task));
        } else {
            responseEntity = list(() -> taskService.listClosedTasks(task, getStaff(request).getId()));
        }

        return responseEntity;

    }

    @GetMapping(value = {"/hr/api/v1/task/list/future", "/hr/api/v1/task/list/future/{isListAll}"})
    public @ResponseBody ResponseEntity<List<Task>> listFutureTasks(Task task,
                                                                    @PathVariable(value = "isListAll", required = false) Boolean isListAll,
                                                                    HttpServletRequest request) {

        ResponseEntity<List<Task>> responseEntity = null;

        if(isListAll) {
            responseEntity = list(() -> taskService.listFutureTasks(task));
        } else {
            responseEntity = list(() -> taskService.listFutureTasks(task, getStaff(request).getId()));
        }

        return responseEntity;

    }

    @PutMapping("/hr/api/v1/task/comment/{taskId}")
    public @ResponseBody ResponseEntity<Boolean> insertTicketComment(@PathVariable("taskId") Long taskId, @RequestBody String comment, HttpServletRequest request) {
        return find(() -> taskService.insertComment(taskId, getStaff(request), comment));
    }

    // Ticket operations
    @PutMapping("/hr/api/v1/task/ticket/reassign/{ticketId}/{technicianId}")
    public @ResponseBody ResponseEntity<SimpleBooleanValue> reassignTicket(@PathVariable("ticketId") Long ticketId, @PathVariable("technicianId") Long technicianId, @RequestBody String reason, HttpServletRequest request) {
        return find(() -> SimpleBooleanValue.of(ticketService.reassign(ticketId, getStaff(request), technicianId, reason)));
    }

    @PutMapping("/hr/api/v1/ticket/close/{ticketId}")
    public @ResponseBody ResponseEntity<SimpleBooleanValue> closeTicket(@PathVariable("ticketId") Long ticketId, @RequestBody String resolution, HttpServletRequest request) {
        return find(() ->  SimpleBooleanValue.of(ticketService.close(ticketId, getStaff(request), resolution)));
    }

    @PutMapping("/hr/api/v1/ticket/reopen/{ticketId}")
    public @ResponseBody ResponseEntity<SimpleBooleanValue> reopenTicket(@PathVariable("ticketId") Long ticketId, @RequestBody String reason, HttpServletRequest request) {
        return find(() -> SimpleBooleanValue.of(ticketService.reopen(ticketId, getStaff(request), reason)));
    }

    // Active Directory Integration
    @PostMapping("/hr/api/v1/task/ad/push/{id}")
    public @ResponseBody ResponseEntity<Boolean> pushStaffFromDB2AD(@PathVariable("id") Long id, @RequestBody List<String> fields) {
        return find(() -> activeDirectoryService.pushStaffFromDB2AD(id, fields));
    }

    @PostMapping("/hr/api/v1/task/ad/pull/{id}")
    public @ResponseBody ResponseEntity<Boolean> pullStaffFromAD2DB(@PathVariable("id") Long id, @RequestBody List<String> fields) {
        return find(() -> activeDirectoryService.pullStaffFromAD2DB(id, fields));
    }

    @GetMapping("/hr/api/v1/task/active-directory/user/{login}")
    public @ResponseBody ResponseEntity<UserAccount> findADUser(@PathVariable("login") String login) {
        UserAccount account = activeDirectoryService.findByLogin(login);
        return find(() -> account);
    }

}
