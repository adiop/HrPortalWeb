package com.warn.hr.business;

import com.warn.hr.business.rest.TicketServiceRestClient;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TicketService {

    @NonNull
    private TicketServiceRestClient ticketServiceRestClient;

    public Boolean reassign(Long id, Staff reassignedBy, Long newTechnicianId, String reason) {

        Staff technician = new Staff();
        technician.setId(newTechnicianId);

        return ticketServiceRestClient.reassign(id, reassignedBy, technician, reason);

    }

    public Boolean close(Long id, Staff closedBy, String resolution) {
        return ticketServiceRestClient.close(id, closedBy, resolution);
    }

    public Boolean reopen(Long id, Staff reopenedBy, String reason) {
        return ticketServiceRestClient.reopen(id, reopenedBy, reason);
    }

}
