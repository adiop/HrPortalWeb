'use strict';

//User service
app.service('userService', function(config, $http, $q) {

    const svc = this;

    svc.loadUser = (form) => {

        Request.get(`/hr/api/v1/user/staff`).call(response => {

            Response.of(response.status)
                .ok(() => {
                    form.user = response.data;
                    svc.isUserAdmin(form.user);
                });

        });

    }

    svc.isUserAdmin = (form) => {

        Request.get(`/hr/api/v1/user/role/admin`).call(response => {

            Response.of(response.status)
                .ok(() => {
                    form.isUserAdmin = response.data;
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