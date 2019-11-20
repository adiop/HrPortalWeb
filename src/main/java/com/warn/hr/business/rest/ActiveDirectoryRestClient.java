package com.warn.hr.business.rest;

import com.warn.hr.persistence.entity.hr.UserAccount;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * Rest client that communicates with the Task Rest Service.
 * To find the connection data see Application.yaml
 */
public interface ActiveDirectoryRestClient extends _RestClient {

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("GET /warn/util/api/v1/active-directory/users/{login}")
    public UserAccount findByLogin(@Param("token") String token,
                                 @Param("login") String login);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("PUT /warn/util/api/v1/active-directory/users/")
    public Boolean update(@Param("token") String token,
                                  UserAccount userAccount);

}
