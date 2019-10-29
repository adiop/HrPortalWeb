package com.warn.hr.business.rest;

import com.warn.hr.persistence.entity.task.TicketTask;
import org.junit.Before;
import org.junit.Test;

public class TicketRestServiceTest {

    private static final String KEY = "";

    private static final String ENDPOINT = "http://warn-apiserv01:6101";

    private TicketRestClient ticketServiceRestClient;

    @Before
    public void setUp() {
        ticketServiceRestClient = _RestClient.of(ENDPOINT, TicketRestClient.class);
    }

    @Test
    public void findByIdTest() {

        Long id = 6042L;

        TicketTask ticket = ticketServiceRestClient.findById(KEY, id);

        System.out.println(ticket);


    }

    @Test
    public void reassignTest() {

        Long id = 6042L;

        ticketServiceRestClient.reassign(KEY, id, 303L, 303L, "Any reason");

    }

}
