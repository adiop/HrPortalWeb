'use strict';

//Job titles service
app.service('portalService', function(config, $http, $q, userService) {

    const svc = this;

    svc.loadUser = (form) => {
        userService.loadUser(form);
    }

    svc.loadTasks = (form) => {

        let uri = '/hr/api/v1/task/list/open/false';

        Request.get(uri).call((response) => {
            Response.of(response.status).ok(() => {
                form.result = response.data;
            });
        });

    }

    svc.initialize = () => {

        // Init the configuration of the requests
        Request.config($http,$q);

    };


    // Init the service
    {
        svc.initialize();
    };

});