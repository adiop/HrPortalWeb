'use strict';

//Job titles service
app.service('managerService', function(config, $http, $q) {

    const svc = this;
    const storageKeyAllManagerById = "allManagerById";
    const storageKeyAllManagerByStaffId = "allManagerByStaffId";

    svc.findById = (id) => {

        let manager = null;

        if(id != undefined) {

            let managerByIdMap = JSON.parse(sessionStorage.getItem(storageKeyAllManagerById));

            if(managerByIdMap[id]) {
                manager = managerByIdMap[id];
            }

        }

        return manager;

    };

    svc.findByStaffId = (staffId) => {

        let manager = null;

        if(staffId != undefined) {

            let managerByStaffIdMap = JSON.parse(sessionStorage.getItem(storageKeyAllManagerByStaffId));

            if(managerByStaffIdMap[staffId]) {
                manager = managerByStaffIdMap[staffId];
            }

        }

        return manager;

    }

    svc.initialize = () => {

        // Init the configuration of the requests
        Request.config($http,$q);

        // Rest the storage
        svc.resetStorage(false);

    };

    svc.resetStorage = (forceReload) => {

        if(!sessionStorage.getItem(storageKeyAllManagerByStaffId) || forceReload) {

            sessionStorage.removeItem(storageKeyAllManagerByStaffId);

            // Load all the job titles at the session storage
            Request.get('/hr/api/v1/manager/map-all-by-staff-id').call((response) => {

                Response.of(response.status).ok(() => {
                    sessionStorage.setItem(storageKeyAllManagerByStaffId, JSON.stringify(response.data));
                });

            });

        }

        if(!sessionStorage.getItem(storageKeyAllManagerById) || forceReload) {

            sessionStorage.removeItem(storageKeyAllManagerById);

            // Load all the job titles at the session storage
            Request.get('/hr/api/v1/manager/map-all-by-id').call((response) => {

                Response.of(response.status).ok(() => {
                    sessionStorage.setItem(storageKeyAllManagerById, JSON.stringify(response.data));
                });

            });

        }

    };

    // Init the service
    {
        svc.initialize();
    };

});