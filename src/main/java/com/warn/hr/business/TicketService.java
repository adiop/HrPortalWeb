package com.warn.hr.business;

import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.task.Task;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TicketService {

    @Autowired
    private TicketServiceTemplate ticketServiceTemplate;

    public Boolean reassign(Long id, Staff reassignedBy, Long newTechnicianId, String reason) {

        Staff technician = new Staff();
        technician.setId(newTechnicianId);

        return ticketServiceTemplate.reassign(id, reassignedBy, technician, reason);

    }

    public Boolean close(Long id, Staff closedBy, String resolution) {
        return ticketServiceTemplate.close(id, closedBy, resolution);
    }

    public Boolean reopen(Long id, Staff reopenedBy, String reason) {
        return ticketServiceTemplate.reopen(id, reopenedBy, reason);
    }

    @Component
    @PropertySource("classpath:task.${spring.profiles.active}.properties")
    public @Data
    class TicketServiceTemplate {

        @Value("${task.api.protocol}")
        private String protocol;

        @Value("${task.api.host}")
        private String host;

        @Value("${task.api.port}")
        private String port;

        @Value("${ticket.api.prefix}")
        private String urlPrefix;

        private RestTemplate restTemplate;

        public TicketServiceTemplate() {
            this.restTemplate = new RestTemplate();
        }

        public Task findById(Long id) {
            String uri = getUri().path("/" + id).toUriString();
            return restTemplate.exchange(uri, HttpMethod.GET, null, Task.class).getBody();
        }

        public Boolean reassign(Long id, Staff reassignedBy, Staff newTechnician, String reason) {
            String uri = getUri().path("/reassign/").path(id.toString()).path("/").path(reassignedBy.getId().toString()).path("/").path(newTechnician.getId().toString()).toUriString();
            return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(reason), new ParameterizedTypeReference<Boolean>(){}).getBody();
        }

        public Boolean close(Long id, Staff closedBy, String resolution) {
            String uri = getUri().path("/close/").path(id.toString()).path("/").path(closedBy.getId().toString()).toUriString();
            return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(resolution), new ParameterizedTypeReference<Boolean>(){}).getBody();
        }

        public Boolean reopen(Long id, Staff reopenedBy, String reason) {
            String uri = getUri().path("/reopen/").path(id.toString()).path("/").path(reopenedBy.getId().toString()).toUriString();
            return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(reason), new ParameterizedTypeReference<Boolean>(){}).getBody();
        }

        private UriComponentsBuilder getUri() {
            return UriComponentsBuilder.newInstance().scheme(protocol).host(host).port(port).path(urlPrefix);
        }

        private <T> HttpEntity<T> getHttpEntity(T body) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("key", "583eccb2-1f67-11e9-ab14-d663bd873d93");
            return new HttpEntity<T>(body, headers);
        }

    }

}
