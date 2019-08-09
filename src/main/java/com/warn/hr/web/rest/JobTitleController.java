package com.warn.hr.web.rest;

import com.warn.hr.business.JobTitleService;
import com.warn.hr.persistence.entity.hr.JobTitle;
import com.warn.hr.web.rest.common.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;

@Slf4j
@RestController
public class JobTitleController extends AbstractController {

    @Autowired
    private JobTitleService jobTitleService;

    @PostMapping("/hr/api/v1/job-title/")
    public @ResponseBody ResponseEntity create(@RequestBody JobTitle entity) {
        return create(() -> String.format("/hr/api/v1/job-title/%d", jobTitleService.insert(entity)));
    }


    @PutMapping("/hr/api/v1/job-title/{id}")
    public @ResponseBody ResponseEntity update(@PathVariable("id") Long id, @RequestBody JobTitle entity) {

        entity.setId(id);

        return update(() -> jobTitleService.update(entity));

    }

    @DeleteMapping("/hr/api/v1/job-title/{id}")
    public @ResponseBody ResponseEntity delete(@PathVariable("id") Long id) {

        JobTitle jobTitle = new JobTitle();
        jobTitle.setId(id);

        return Match(jobTitleService.delete(jobTitle)).of(Case($(l -> l.booleanValue()), delete(() -> Boolean.TRUE)),
                                                          Case($(), unprocessableEntity("The job title cannot be removed! There are Employees associated with this job title.")));

    }

    @GetMapping("/hr/api/v1/job-title/{id}")
    public ResponseEntity<JobTitle> findById(@PathVariable("id") Long id) {
        return find(() -> jobTitleService.findById(id));
    }

    @GetMapping("/hr/api/v1/job-title/list")
    public @ResponseBody ResponseEntity<List<JobTitle>> list(JobTitle jobTitle) {
        return list(() -> jobTitleService.list(jobTitle));
    }

    @GetMapping("/hr/api/v1/job-title/list-all")
    public @ResponseBody ResponseEntity<List<JobTitle>> listAll() {
        return list(() -> jobTitleService.list(new JobTitle()));
    }

}
