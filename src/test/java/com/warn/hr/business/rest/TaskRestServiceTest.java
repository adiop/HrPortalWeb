package com.warn.hr.business.rest;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TaskRestServiceTest {

    private static final String KEY = "";

    private static final String ENDPOINT = "http://warn-apiserv01:6101";

    private TaskRestClient ticketRestServiceTest;

    @Before
    public void setUp() {
        ticketRestServiceTest = _RestClient.of(ENDPOINT, TaskRestClient.class);
    }

    @Test
    public void listOpenTest() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("taskStaff.fullName", "david");

        System.out.println(ticketRestServiceTest.listOpenTasks(KEY, parameters));


    }


    @Test
    public void listOpenByTechnicianTest() {

        Long technicianId = 303L;

        Map<String, String> parameters = new HashMap<>();

        ticketRestServiceTest.listOpenTasks(KEY, technicianId, parameters).forEach(System.out::println);

    }

}
