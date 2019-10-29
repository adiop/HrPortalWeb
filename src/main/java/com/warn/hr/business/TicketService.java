package com.warn.hr.business;

import com.warn.hr.business.rest.TicketRestClient;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    @NonNull
    private String taskRestServiceKey;

    @NonNull
    private TicketRestClient ticketServiceRestClient;

    public Boolean reassign(Long id, Staff reassignedBy, Long newTechnicianId, String reason) {
        return ticketServiceRestClient.reassign(taskRestServiceKey,
                id,
                reassignedBy.getId(),
                newTechnicianId,
                reason);
    }

    public Boolean close(Long id, Staff closedBy, String resolution) {
        return ticketServiceRestClient.close(taskRestServiceKey,
                id,
                closedBy.getId(),
                resolution);
    }

    public Boolean reopen(Long id, Staff reopenedBy, String reason) {
        return ticketServiceRestClient.reopen(taskRestServiceKey,
                id,
                reopenedBy.getId(),
                reason);
    }

}
