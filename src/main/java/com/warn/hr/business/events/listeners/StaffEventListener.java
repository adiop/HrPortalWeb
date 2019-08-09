package com.warn.hr.business.events.listeners;

import com.warn.hr.business.TaskService;
import com.warn.hr.business.events.StaffEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StaffEventListener implements ApplicationListener<StaffEvent> {

    @Autowired
    private TaskService taskService;

    @Override
    public void onApplicationEvent(StaffEvent event) {

        switch (event.getType()) {
            case NEW_STAFF:
                taskService.createTaskForNewStaff(event.getCreatedBy(), event.getTargetStaff(), event.getEffectiveDate());
                break;
            case UPDATE_STAFF:
                taskService.createTaskForUpdateStaff(event.getCreatedBy(), event.getTargetStaff());
                break;
            case TERM_STAFF:
                taskService.createTaskForTermStaff(event.getCreatedBy(), event.getTargetStaff(), event.getEffectiveDate());
                break;
        }

    }

}
