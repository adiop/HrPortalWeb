'user strict'

// staff-resources Service
app.service('staffResourceService', function (config, $http, $q) {

    const svc = this;
    const storageKeyAllStaffResources = "allStaffResources";
    const storageKeyAllTypeOptions = "allStaffResourceTypes";
    const storageKeyAllDefaultOptions = "allStaffResourceDefaults";

    svc.insert = (form, messages) => {

        Request.post('/hr/api/v1/staff-resource/', svc.formToData(form)).call((response) => {

            Response.of(response.status)
                .create(() => {
                    messages.successMessage = 'Staff resource inserted with success!';
                }).otherwise(() => {
                    messages.errorMessage = 'An error has occurred!';
                }).then(() => {
                    svc.resetStorage(true);
                });

        });

    };

    svc.update = (form, messages) => {

        Request.put(`/hr/api/v1/staff-resource/${form.id}`, svc.formToData(form)).call((response) => {

            Response.of(response.status)
                .ok(() => {
                    messages.successMessage = 'Staff resource updated with success!';
                }).otherwise(() => {
                    messages.errorMessage = 'An error has occurred!';
                }).then(() => {
                    svc.resetStorage(true);
                });

        });

    };

    svc.delete = (id, messages) => {

        Request.delete(`/hr/api/v1/staff-resource/${id}`).call((response) => {

            Response.of(response.status)
                .ok(() => {
                    messages.successMessage = 'Staff resource deleted with success!';
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

        Request.get(`/hr/api/v1/staff-resource/${id}`).call((response) => {

            form.loading = false;

            Response.of(response.status).ok(() => {

                form.id = response.data.id;
                form.name = response.data.name;
                form.description = response.data.description;
                form.technician = response.data.technician;

                form.selectType(response.data.type);

                let defaultResourceName = '';

                if(response.data.isEmployeeDefault) {
                    defaultResourceName = 'ALL-EMPLOYEES';
                } else if(response.data.isTeamLeaderDefault) {
                    defaultResourceName = 'ALL-TEAM-LEADERS';
                }

                form.selectDefaultResource(defaultResourceName);

            });

        });

    }

    svc.get = (id) => {

        return svc.listAll().filter((staffResource) => {
            return staffResource.id === id;
        }).shift();

    };

    svc.listAllApplicationAccesses = () => {
        return svc.list({type: {value: 'APPLICATION_ACCESS'}, defaultResource: {value: ''}});
    };

    svc.listAllAssets = () => {
        return svc.list({type: {value: 'ASSET'}, defaultResource: {value: ''}});
    };

    svc.listAllEmailGroups = () => {
        return svc.list({type: {value: 'EMAIL_GROUP'}, defaultResource: {value: ''}});
    };

    svc.list = (form) => {

        return svc.listAll().filter((staffResource) => {

            let filter = true;

            if(form.name) {
                filter &= staffResource.name.toLowerCase().indexOf(form.name.toLowerCase()) != -1;
            }

            if(form.type.value) {
                filter &= staffResource.type === form.type.value;
            }

            if(form.defaultResource.value) {
                filter &= staffResource.isEmployeeDefault === ('ALL-EMPLOYEES' === form.defaultResource.value);
                filter &= staffResource.isTeamLeaderDefault === ('ALL-TEAM-LEADERS' === form.defaultResource.value);
            }

            return filter;

        }).slice();

    };

    svc.listAll = () => {
        return JSON.parse(sessionStorage.getItem(storageKeyAllStaffResources));
    };

    svc.listAllTypes = () => {
        return JSON.parse(sessionStorage.getItem(storageKeyAllTypeOptions));
    };

    svc.listAllDefaults = () => {
        return JSON.parse(sessionStorage.getItem(storageKeyAllDefaultOptions));
    };

    svc.loadTechnician = (object) => {

        Request.get('/hr/api/v1/staff-resource/technician/'.concat(object.id)).call((response) => {

            Response.of(response.status).ok(() => {
                object.technician = response.data;
            });

        });

    };

    svc.formToData = ({name: name, defaultResource: defaultResource, type: type, technician: technician, description: description}) => {

        return {name: name,
                isEmployeeDefault: 'ALL-EMPLOYEES' === defaultResource.value,
                isTeamLeaderDefault: 'ALL-TEAM-LEADERS' === defaultResource.value,
                description: description,
                type: type.value,
                technician: technician};

    };

    svc.initialize = () => {

        // Init the configuration of the requests
        Request.config($http,$q);

        // Rest the storage
        svc.resetStorage(false);

    };

    svc.resetStorage = (forceReload) => {

        if(!sessionStorage.getItem(storageKeyAllStaffResources) || forceReload) {

            sessionStorage.removeItem(storageKeyAllStaffResources);

            // Load all the staff resources at the session storage
            Request.get('/hr/api/v1/staff-resource/list-all').call((response) => {

                Response.of(response.status).ok(() => {
                    sessionStorage.setItem(storageKeyAllStaffResources, JSON.stringify(response.data));
                });

            });

            sessionStorage.setItem(storageKeyAllTypeOptions, JSON.stringify([{label: 'Asset', value: 'ASSET'},
                                                                        {label: 'Application Access', value: 'APPLICATION_ACCESS'},
                                                                        {label: 'Email Group', value: 'EMAIL_GROUP'}]));

            sessionStorage.setItem(storageKeyAllDefaultOptions, JSON.stringify([{label: 'For all employees', value: 'ALL-EMPLOYEES'},
                                                                                {label: 'For Team Leaders', value: 'ALL-TEAM-LEADERS'}]));

        }

    };

    // Init the service
    {
        svc.initialize();
    };

});