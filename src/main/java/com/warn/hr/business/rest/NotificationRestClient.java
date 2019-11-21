package com.warn.hr.business.rest;

import com.warn.hr.persistence.entity.hr.Notification;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * Rest client that communicates with the Task Rest Service.
 * To find the connection data see Application.yaml
 */
public interface NotificationRestClient extends _RestClient {

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("POST /warn/util/api/v1/notification/")
    public Boolean sendEmail(@Param("token") String token,
                              Notification notification);

}
