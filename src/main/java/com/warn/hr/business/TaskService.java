package com.warn.hr.business;

import com.warn.hr.business.rest.TaskServiceRestClient;
import com.warn.hr.persistence.entity.hr.StaffResource;
import com.warn.hr.persistence.entity.hr.staff.StaffHistory;
import com.warn.hr.persistence.entity.hr.staff.Technician;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.task.Task;
import com.warn.hr.persistence.entity.task.TicketTask;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService  {

    private static Technician helpDeskTechnician;

    @NonNull
    private TaskServiceRestClient taskServiceRestClient;

    @NonNull
    private StaffService staffService;

    @NonNull
    private StaffHistoryService staffHistoryService;

    @NonNull
    private StaffResourceService staffResourceService;

    public Task findById(Long id) {

        Task task = taskServiceRestClient.findById(id);

        // Load staff details
        task.setTaskStaff(staffService.findById(task.getTaskStaff().getId()));

        return task;

    }

    public Task findById(Long id, Long technicianId) {

        Task task = taskServiceRestClient.findById(id, technicianId);

        // Load staff details
        task.setTaskStaff(staffService.findById(task.getTaskStaff().getId()));

        return task;

    }

    public List<Task> listOpenTasks(Task task) {
        return taskServiceRestClient.listOpenTasks(task);
    }

    public List<Task> listOpenTasks(Task task, Long technicianId) {
        return taskServiceRestClient.listOpenTasks(task, technicianId);
    }

    public List<Task> listClosedTasks(Task task) {
        return taskServiceRestClient.listClosedTasks(task);
    }

    public List<Task> listClosedTasks(Task task, Long technicianId) {
        return taskServiceRestClient.listClosedTasks(task, technicianId);
    }

    public List<Task> listFutureTasks(Task task) {
        return taskServiceRestClient.listFutureTasks(task);
    }

    public List<Task> listFutureTasks(Task task, Long technicianId) {
        return taskServiceRestClient.listFutureTasks(task, technicianId);
    }

    public Long createTaskForNewStaff(Staff createdBy, Staff taskStaff, Date effectiveDate) {

        List<TicketTask> ticketTasks = new LinkedList<>();

        Technician technician = getHelpdeskTechnician();

        TicketTask ticket = new TicketTask();
        ticket.setTitle("AD information");
        ticket.setDescription("(+) The AD information must be created for the Staff member");
        ticket.setTechnician(technician);

        ticketTasks.add(ticket);

        // Added staff resources
        Optional.ofNullable(taskStaff.getStaffResources())
                .ifPresent(staffResources -> ticketTasks.addAll(createAddedResourcesTicketTasks(staffResources.stream().map(sr -> sr.getId()).collect(Collectors.toList()))));

        Task task = new Task();
        task.setEffectiveDate(effectiveDate);
        task.setType(Task.Type.NEW_STAFF);
        task.setCreatedBy(createdBy);
        task.setTaskStaff(taskStaff);
        task.setTicketTasks(ticketTasks);

        return taskServiceRestClient.createTask(task);
    }

    public Long createTaskForUpdateStaff(Staff createdBy, Staff taskStaff) {

        Long taskId = 0L;
        List<TicketTask> ticketTasks = new LinkedList<>();

        StaffHistory staffHistory = staffHistoryService.findLastChange(taskStaff.getId());

        // Flagged employee
        if(staffHistory.hasDataFlag(taskStaff)) {

            Technician technician = getHelpdeskTechnician();

            TicketTask ticket = new TicketTask();
            ticket.setTitle("Employee flagged");
            ticket.setDescription("The Employee was flagged - " + taskStaff.getComments());
            ticket.setTechnician(technician);

            ticketTasks.add(ticket);

        }

        // Information change
        if(staffHistory.hasDataChanges(taskStaff)) {

            Technician technician = getHelpdeskTechnician();

            TicketTask ticket = new TicketTask();
            ticket.setTitle("AD information has changed");
            ticket.setDescription("The AD information must be updated for the Staff member");
            ticket.setTechnician(technician);

            ticketTasks.add(ticket);

        }

        // Added staff resources
        ticketTasks.addAll(createAddedResourcesTicketTasks(staffHistory.addedStaffResourcesIds(taskStaff)));

        // Removed staff resources
        ticketTasks.addAll(createRemovedResourcesTicketTasks(staffHistory.removedStaffResourcesIds(taskStaff)));

        // Create task
        if(!ticketTasks.isEmpty()) {

            // Task
            Task task = new Task();
            task.setType(Task.Type.UPDATE_STAFF);
            task.setCreatedBy(createdBy);
            task.setTaskStaff(taskStaff);
            task.setTicketTasks(ticketTasks);

            taskId = taskServiceRestClient.createTask(task);

        }

        return taskId;

    }

    public Long createTaskForTermStaff(Staff createdBy, Staff taskStaff, Date effectiveDate) {

        List<TicketTask> ticketTasks = new LinkedList<>();

        Technician technician = getHelpdeskTechnician();

        TicketTask ticket = new TicketTask();
        ticket.setTitle("AD information");
        ticket.setDescription("(-) The AD information must be removed for the Staff member");
        ticket.setTechnician(technician);

        ticketTasks.add(ticket);

        // Removed staff resources
        Optional.ofNullable(taskStaff.getStaffResources())
                .ifPresent(staffResources -> ticketTasks.addAll(createRemovedResourcesTicketTasks(staffResources.stream().map(sr -> sr.getId()).collect(Collectors.toList()))));


        Task task = new Task();
        task.setEffectiveDate(effectiveDate);
        task.setType(Task.Type.TERM_STAFF);
        task.setCreatedBy(createdBy);
        task.setTaskStaff(taskStaff);
        task.setTicketTasks(ticketTasks);

        return taskServiceRestClient.createTask(task);

    }

    public Boolean insertComment(long taskId, Staff createdBy, String comment) {
        return taskServiceRestClient.insertComment(taskId, createdBy, comment);
    }

    private List<TicketTask> createAddedResourcesTicketTasks(List<Long> addedStaffResourceIdList) {

        return addedStaffResourceIdList.stream().map(staffResourceId -> {

            StaffResource staffResource = staffResourceService.findById(staffResourceId);

            TicketTask ticket = new TicketTask();
            ticket.setTitle(staffResource.getName());
            ticket.setDescription(String.format("(+) The staff resource \"%s\" must be added to the staff member", staffResource.getName()));
            ticket.setTechnician(staffResource.getTechnician());

            return ticket;

        }).collect(Collectors.toList());

    }

    private List<TicketTask> createRemovedResourcesTicketTasks(List<Long> removedStaffResourceIdList) {

        return removedStaffResourceIdList.stream().map(staffResourceId -> {

            StaffResource staffResource = staffResourceService.findById(staffResourceId);

            TicketTask ticket = new TicketTask();
            ticket.setTitle(staffResource.getName());
            ticket.setDescription(String.format("(-) The staff resource \"%s\" must be removed from the staff member", staffResource.getName()));
            ticket.setTechnician(staffResource.getTechnician());

            return ticket;

        }).collect(Collectors.toList());

    }

    private Technician getHelpdeskTechnician() {

        if(Objects.isNull(helpDeskTechnician)) {
            helpDeskTechnician = staffResourceService.findById(35L).getTechnician();
        }

        return helpDeskTechnician;

    }

}
