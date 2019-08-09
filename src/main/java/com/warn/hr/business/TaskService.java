package com.warn.hr.business;

import com.warn.hr.persistence.entity.hr.StaffResource;
import com.warn.hr.persistence.entity.hr.staff.StaffHistory;
import com.warn.hr.persistence.entity.hr.staff.Technician;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.task.Task;
import com.warn.hr.persistence.entity.task.TicketTask;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService  {

    @Autowired
    private TaskServiceTemplate taskServiceTemplate;

    @Autowired
    private StaffService staffService;

    @Autowired
    private StaffHistoryService staffHistoryService;

    @Autowired
    private StaffResourceService staffResourceService;

    public Task findById(Long id) {

        Task task = taskServiceTemplate.findById(id);

        // Load staff details
        task.setTaskStaff(staffService.findById(task.getTaskStaff().getId()));

        return task;

    }

    public Task findById(Long id, Long technicianId) {

        Task task = taskServiceTemplate.findById(id, technicianId);

        // Load staff details
        task.setTaskStaff(staffService.findById(task.getTaskStaff().getId()));

        return task;

    }

    public List<Task> listOpenTasks(Task task) {
        return taskServiceTemplate.listOpenTasks(task);
    }

    public List<Task> listOpenTasks(Task task, Long technicianId) {
        return taskServiceTemplate.listOpenTasks(task, technicianId);
    }

    public List<Task> listClosedTasks(Task task) {
        return taskServiceTemplate.listClosedTasks(task);
    }

    public List<Task> listClosedTasks(Task task, Long technicianId) {
        return taskServiceTemplate.listClosedTasks(task, technicianId);
    }

    public List<Task> listFutureTasks(Task task) {
        return taskServiceTemplate.listFutureTasks(task);
    }

    public List<Task> listFutureTasks(Task task, Long technicianId) {
        return taskServiceTemplate.listFutureTasks(task, technicianId);
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

        return taskServiceTemplate.createTask(task);
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

            taskId = taskServiceTemplate.createTask(task);

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

        return taskServiceTemplate.createTask(task);

    }

    public Boolean insertComment(long taskId, Staff createdBy, String comment) {
        return taskServiceTemplate.insertComment(taskId, createdBy, comment);
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
        return staffResourceService.findById(35L).getTechnician();
    }

    @Component
    @PropertySource("classpath:task.${spring.profiles.active}.properties")
    public @Data class TaskServiceTemplate {

        @Value("${task.api.protocol}")
        private String protocol;

        @Value("${task.api.host}")
        private String host;

        @Value("${task.api.port}")
        private String port;

        @Value("${task.api.prefix}")
        private String urlPrefix;

        private RestTemplate restTemplate;

        public TaskServiceTemplate() {
            this.restTemplate = new RestTemplate();
        }

        public Long createTask(Task task) {
            String uri = getUri().toUriString();
            ResponseEntity responseEntity = restTemplate.exchange(uri, HttpMethod.POST, getHttpEntity(task), ResponseEntity.class).getBody();
            return null;
        }

        public Task findById(Long id) {
            String uri = getUri().path("/" + id).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, null, Task.class).getBody();
        }

        public Task findById(Long id, Long technicianId) {
            String uri = getUri().path("/" + id).path("/" + technicianId).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, null, Task.class).getBody();
        }

        public Boolean insertComment(Long id, Staff createdBy, String comment) {
            String uri = getUri().path("/comment/").path(id.toString()).path("/").path(createdBy.getId().toString()).toUriString();
            return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(comment), new ParameterizedTypeReference<Boolean>(){}).getBody();
        }

        public List<Task> listOpenTasks(Task task) {
            String uri = getUri().path("/list/open/").queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
        }

        public List<Task> listOpenTasks(Task task, Long technicianId) {
            String uri = getUri().path("/list/open/").path("/" + technicianId).queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
        }

        public List<Task> listClosedTasks(Task task) {
            String uri = getUri().path("/list/closed/").queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
        }

        public List<Task> listClosedTasks(Task task, Long technicianId) {
            String uri = getUri().path("/list/closed/").path("/" + technicianId).queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
        }

        public List<Task> listFutureTasks(Task task) {
            String uri = getUri().path("/list/future/").queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
        }

        public List<Task> listFutureTasks(Task task, Long technicianId) {
            String uri = getUri().path("/list/future/").path("/" + technicianId).queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
        }

        private UriComponentsBuilder getUri() {
            return UriComponentsBuilder.newInstance().scheme(protocol).host(host).port(port).path(urlPrefix);
        }

        private <T> HttpEntity<T> getHttpEntity(T body) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("key", "583eccb2-1f67-11e9-ab14-d663bd873d93");
            return new HttpEntity<T>(body, headers);
        }

    }

}
