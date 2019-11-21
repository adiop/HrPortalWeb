package com.warn.hr.business;

import com.warn.hr.Application;
import com.warn.hr.business.rest.TicketRestClient;
import com.warn.hr.business.rest._RestClient;
import com.warn.hr.persistence.entity.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

public class TicketServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void reassign() {

        TicketRestClient ticketRestClient = _RestClient.of("http://warn-docker-stg-intranet-api01.warn.com", TicketRestClient.class);

        ticketRestClient.reassign("", 2087L, 303L, 85L, "good reason");

    }

    @Test
    public void close() {
    }

    @Test
    public void reopen() {
    }
}
