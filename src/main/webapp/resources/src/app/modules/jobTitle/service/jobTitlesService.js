'use strict';

//Job titles service
app.service('jobTitlesService', function(config, $http, $q, departmentsService, staffResourceService) {

    const svc = this;
    const storageKeyAllJobTitles = "allJobTitles";

    svc.insert = (form, messages) => {

        Request.post('/hr/api/v1/job-title/', svc.formToData(form)).call((response) => {

            Response.of(response.status)
                .create(() => {
                    messages.successMessage = 'Job title inserted with success!';
                }).otherwise(() => {
                    messages.errorMessage = 'An error has occurred!';
                }).then(() => {
                    svc.resetStorage(true);
                });

        });

    };

    svc.update = (form, messages) => {

        Request.put(`/hr/api/v1/job-title/${form.id}`, svc.formToData(form)).call((response) => {

            Response.of(response.status)
                .ok(() => {
                    messages.successMessage = 'Job title updated with success!';
                }).otherwise(() => {
                    messages.errorMessage = 'An error has occurred!';
                }).then(() => {
                    svc.resetStorage(true);
                });

        });

    };

    svc.delete = (id, messages) => {

        Request.delete(`/hr/api/v1/job-title/${id}`).call((response) => {

            Response.of(response.status)
                .ok(() => {
                    messages.successMessage = 'Job title deleted with success!';
                }).unprocessableEntity(() => {
                    messages.errorMessage = response.data.message;
                }).otherwise(() => {
                    messages.errorMessage = 'An error has occurred!';
                }).then(() => {
                    svc.resetStorage(true);
                });

        });

    };

    svc.findById = (id, form) => {

        form.loading = true;

        Request.get(`/hr/api/v1/job-title/${id}`).call((response) => {

            form.loading = false;

            Response.of(response.status).ok(() => {

                form.id = response.data.id;
                form.name = response.data.name;
                form.description = response.data.description;

                form.selectDepartment(response.data.department.id);

                form.selectStaffResources(response.data.staffResources);

            });

        });

    }

    svc.get = (id) => {

        return svc.listAll().filter((jobTitle) => {
            return jobTitle.id === id;
        }).shift();

    };

    svc.list = (form) => {

        return svc.listAll().filter((jobTitle) => {

            let filter = true;

            if(form.name) {
                filter &= jobTitle.name.toLowerCase().indexOf(form.name.toLowerCase()) != -1;
            }

            if(form.department.value) {
                filter &= jobTitle.department.id === form.department.value;
            }

            return filter;

        }).slice();

    };

    svc.listAll = () => {
        return JSON.parse(sessionStorage.getItem(storageKeyAllJobTitles));
    };

    svc.formToData = ({name: name, description: description, department: department, applicationAccessList: applicationAccessList, emailGroupList: emailGroupList, assetList: assetList}) => {

        return {name: name,
                description: description,
                department: {id: department.value},
                staffResources: []
                                .concat(applicationAccessList.filter(r => r.selected === true).map(r => {return {id: r.value}}))
                                .concat(emailGroupList.filter(r => r.selected === true).map(r => {return {id: r.value}}))
                                .concat(assetList.filter(r => r.selected === true).map(r => {return {id: r.value}}))};
    }

    svc.initialize = () => {

        // Init the configuration of the requests
        Request.config($http,$q);

        // Rest the storage
        svc.resetStorage(false);

    };

    svc.resetStorage = (forceReload) => {

        if(!sessionStorage.getItem(storageKeyAllJobTitles) || forceReload) {

            sessionStorage.removeItem(storageKeyAllJobTitles);

            // Load all the job titles at the session storage
            Request.get('/hr/api/v1/job-title/list-all').call((response) => {

                Response.of(response.status).ok(() => {
                    sessionStorage.setItem(storageKeyAllJobTitles, JSON.stringify(response.data));
                });

            });

        }

    };

    // Init the service
    {
        svc.initialize();
    };

});