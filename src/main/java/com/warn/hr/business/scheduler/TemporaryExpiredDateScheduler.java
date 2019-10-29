package com.warn.hr.business.scheduler;

import com.warn.hr.business.NotificationService;
import com.warn.hr.persistence.dao.TemporaryDAO;
import com.warn.hr.persistence.entity.hr.staff.Temporary;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TemporaryExpiredDateScheduler {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/YYYY");

    @NonNull
    private TemporaryDAO temporaryDAO;

    @NonNull
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 12 * * *")
    public void execute() {

        List<Temporary> expiringTemporaries = temporaryDAO.listExpiringTemporaries();

        if(!expiringTemporaries.isEmpty()) {

            StringBuilder notificationMessage = new StringBuilder();
            notificationMessage.append("The following temporaries are finishing their contracts:").append("</br></br>");

            expiringTemporaries.forEach(temporary -> {
                notificationMessage.append(temporary.getFullName())
                        .append(" - ")
                        .append(DATE_FORMAT.format(temporary.getContractEndDate()))
                        .append("</br></br>");
            });

            notificationMessage.append("Go to the HR Portal to update their contract end date.");

            notificationService.sendEmail("HR Portal - Temporaries ending contract", notificationMessage.toString());

        }



    }

}
