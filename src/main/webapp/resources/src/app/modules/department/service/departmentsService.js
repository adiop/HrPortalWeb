'user strict'

// departments Service
app.service('departmentsService', function (config, $http, $q) {

    const svc = this;
    const storageKeyAllDepartments = "allDepartments";

    svc.insert = (form, messages) => {

        Request.post('/hr/api/v1/department/', svc.formToData(form)).call((response) => {

            Response.of(response.status)
                .create(() => {
                    messages.successMessage = 'Department inserted with success!';
                }).otherwise(() => {
                    messages.errorMessage = 'An error has occurred!';
                }).then(() => {
                    svc.resetStorage(true);
                });

        });

    };

    svc.update = (form, messages) => {

        Request.put('/hr/api/v1/department/'.concat(form.id), svc.formToData(form)).call((response) => {

            Response.of(response.status)
                .ok(() => {
                    messages.successMessage = 'Department updated with success!';
                }).otherwise(() => {
                    messages.errorMessage = 'An error has occurred!';
                }).then(() => {
                    svc.resetStorage(true);
                });

        });

    };

    svc.delete = (id, messages) => {

        return new Promise(() => {

            Request.delete('/hr/api/v1/department/'.concat(id)).call((response) => {

                Response.of(response.status)
                    .ok(() => {
                        messages.successMessage = 'Department deleted with success!';
                    }).unprocessableEntity(() => {
                        messages.errorMessage = response.data.message;
                    }).otherwise(() => {
                        messages.errorMessage = 'An error has occurred!';
                    }).then(() => {
                        svc.resetStorage(true);
                    });

            });

        });

    };

    svc.findById = (id, form) => {

        form.loading = true;

        Request.get(`/hr/api/v1/department/${id}`).call((response) => {

            form.loading = false;

            Response.of(response.status).ok(() => {

                form.id = response.data.id;
                form.name = response.data.name;
                form.departmentCode = response.data.departmentCode;
                form.description = response.data.description;
                form.jobTitles = response.data.jobTitles;

            });

        });

    }

    svc.list = (form) => {

        return svc.listAll().filter((department) => {

            let filter = true;

            if(form.name) {
                filter &= department.name.toLowerCase().indexOf(form.name.toLowerCase()) != -1;
            }

            return filter;

        }).slice();

    };

    svc.listAll = () => {
        return JSON.parse(sessionStorage.getItem(storageKeyAllDepartments));
    };

    svc.formToData = ({name: name, departmentCode: departmentCode, description: description}) => {

        return {name: name,
                  departmentCode: departmentCode,
                  description: description};

    };

    svc.initialize = () => {

        // Init the configuration of the requests
        Request.config($http,$q);

        // Rest the storage
        svc.resetStorage(false);

    };

    svc.resetStorage = (forceReload) => {

        if(!sessionStorage.getItem(storageKeyAllDepartments) || forceReload) {

            sessionStorage.removeItem(storageKeyAllDepartments);

            // Load all the departments at the session storage
            Request.get('/hr/api/v1/department/list-all').call((response) => {

                Response.of(response.status).ok(() => {
                    sessionStorage.setItem(storageKeyAllDepartments, JSON.stringify(response.data));
                });

            })

        }

    };

    // Init the service
    {
        svc.initialize();
    };

});