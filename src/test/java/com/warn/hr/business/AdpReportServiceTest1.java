package com.warn.hr.business;

import com.warn.hr.Application;
import com.warn.hr.persistence.dao.ADPEmployeeDAO;
import com.warn.hr.persistence.entity.hr.staff.adp.ADPEmployee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class AdpReportServiceTest1 {

    private static final String TEST_FILE_PATH = "/projects/Employee Data for IT.xlsx";

    private byte[] fileContent;

    @Mock
    private ADPEmployeeDAO adpEmployeeDAO;

    @InjectMocks
    private AdpReportService adpReportService;

    @Before
    public void setUp() throws Exception {
        this.fileContent = Files.readAllBytes(Paths.get(TEST_FILE_PATH));
    }

    @Test
    public void uploadFile() {
        adpReportService.uploadFile(this.fileContent);
    }

    @Test
    public void listADPEmployeeMap() {


        adpReportService.uploadFile(this.fileContent);
        Map<String, List<ADPEmployee>> adpEmployeesMap = adpReportService.listADPEmployeeMap();

        System.out.println("############# UPDATED #############");
        adpEmployeesMap.get("UPDATED").stream().forEach(System.out::println);

        System.out.println("############# INSERTED #############");
        adpEmployeesMap.get("INSERTED").stream().forEach(System.out::println);

        System.out.println("############# REMOVED #############");
        adpEmployeesMap.get("REMOVED").stream().forEach(System.out::println);

        System.out.println("############# ERROR #############");
        adpEmployeesMap.get("ERROR").stream().forEach(System.out::println);

    }

    @Test
    public void listUpdatedEmployees() {

        adpReportService.uploadFile(this.fileContent);
        List<ADPEmployee> adpEmployees = adpReportService.listUpdatedEmployees();

        adpEmployees.stream().forEach(System.out::println);

    }

    @Test
    public void listInsertedEmployees() {
    }

    @Test
    public void listRemovedEmployees() {
    }

    @Test
    public void listErrorEmployees() {

        adpReportService.uploadFile(this.fileContent);
        List<ADPEmployee> adpEmployees = adpReportService.listErrorEmployees();

        adpEmployees.stream().forEach(System.out::println);

    }

}
