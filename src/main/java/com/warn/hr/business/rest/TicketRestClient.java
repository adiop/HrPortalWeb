package com.warn.hr.business.rest;

import com.warn.hr.persistence.entity.task.TicketTask;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * Rest client that communicates with the Ticket Rest Service.
 * To find the connection data see Application.yaml
 */
public interface TicketRestClient extends _RestClient {

    @Headers({"key: {token}"})
    @RequestLine("GET /api/hr/v1/ticket/{id}")
    public TicketTask findById(@Param("token") String token,
                               @Param("id") Long id);

    @Headers({"key: {token}"})
    @RequestLine("PUT /api/hr/v1/ticket/reassign/{id}/{reassignedById}/{technicianId}")
    public Boolean reassign(@Param("token") String token,
                            @Param("id") Long id,
                            @Param("reassignedById") Long reassignedBy,
                            @Param("technicianId") Long newTechnicianId,
                            String reason);

    @Headers({"key: {token}"})
    @RequestLine("PUT /api/hr/v1/ticket/close/{id}/{closedById}")
    public Boolean close(@Param("token") String token,
                         @Param("id") Long id,
                         @Param("closedById") Long closedById,
                         String resolution);

    @Headers({"key: {token}"})
    @RequestLine("PUT /api/hr/v1/ticket/reopen/{id}/{reopenedById}")
    public Boolean reopen(@Param("token") String token,
                          @Param("id") Long id,
                          @Param("reopenedById") Long reopenedBy,
                          String reason);

}
