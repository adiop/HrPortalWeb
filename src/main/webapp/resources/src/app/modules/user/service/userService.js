'use strict';

//User service
app.service('userService', function(config, $http, $q) {

    const svc = this;

    svc.loadUser = (form) => {

        if(svc._user === null) {

            Request.get(`/hr/api/v1/user/staff`).call(response => {

                Response.of(response.status)
                    .ok(() => {
                        svc._user = response.data;
                        svc.isUserAdmin(svc._user);
                        form.user = svc._user;
                    });

            });

        } else {
            form.user = svc._user;
        }

    }

    svc.isUserAdmin = (form) => {

        if(svc._isUserAdmin === null) {
            Request.get(`/hr/api/v1/user/role/admin`).call(response => {

                Response.of(response.status)
                    .ok(() => {
                        svc._isUserAdmin = response.data;
                        form.isUserAdmin = svc._isUserAdmin;
                    });

            });

        } else {
            form.isUserAdmin = svc._isUserAdmin;
        }

    }

    svc.initialize = () => {

        svc._user = null;
        svc._isUserAdmin = null;

        // Init the configuration of the requests
        Request.config($http,$q);

    };


    // Init the service
    {
        svc.initialize();
    };

});
