package com.warn.hr.business.rest;

import com.warn.hr.persistence.entity.hr.UserAccount;
import com.warn.hr.persistence.entity.task.TicketTask;
import org.junit.Before;
import org.junit.Test;

public class ActiveDirectoryRestServiceTest {

    private static final String KEY = "";

    private static final String ENDPOINT = "http://warn-apiserv01:7002";

    private ActiveDirectoryRestClient activeDirectoryRestClient;

    @Before
    public void setUp() {
        activeDirectoryRestClient = _RestClient.of(ENDPOINT, ActiveDirectoryRestClient.class);
    }

    @Test
    public void findByIdTest() {

        UserAccount userAccount = activeDirectoryRestClient.findByLogin(KEY, "u2838");

        System.out.println(userAccount);


    }

}
