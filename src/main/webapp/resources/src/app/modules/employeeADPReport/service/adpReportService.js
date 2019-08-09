'use strict';

//Employee service
app.service('adpReportService', function(config, $http, $q) {

    const svc = this;
    const loadingImage = 'data:image/gif;base64,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';

    // Upload the employee picture
    svc.uploadFile = (form, messages) => {

        messages.successMessage = null;
        messages.errorMessage = null;
        let adpFile = form.adpFile;


        setTimeout(() => {

                Request.upload('/hr/api/v1/adp-report/upload', form.adpFile).call((response) => {

                    Response.of(response.status).ok(() => {

                        messages.successMessage = 'File uploaded with success! Loading the report...';
                        form.image = loadingImage;
                        form.map = null;
                        form.adpFile = null;

                        Request.get('/hr/api/v1/adp-report/list').call((response) => {

                            Response.of(response.status).ok(() => {
                                form.map = response.data;
                                messages.successMessage = 'Report loaded with success!';
                                form.image = null;
                            }).otherwise(() => {
                                messages.successMessage = null;
                                messages.errorMessage = 'An error has occurred!';
                                form.image = null;
                            });
                        });

                    }).otherwise(() => {
                        messages.errorMessage = 'An error has occurred!';
                        form.image = null;
                    }).then(() => {
                        form.adpFile = null;
                    });

                });

            },
            2000);

    };

    // Load the default image for the staff
    svc.loadDefaultStaffImage = (staff) => {
        staff.image = defaultStaffImage;
    }

    svc.update = (form, messages, okCallBackFunction, errorCallBackFunction, thenCallBackFunction) => {

        let _okCallBackFunction = () => {
            messages.successMessage = 'Employee updated with success!';
        };

        let _errorCallBackFunction = () => {
            messages.successMessage = 'An error has occurred!';
        };

        if(okCallBackFunction) {
            _okCallBackFunction = okCallBackFunction;
        }

        if(errorCallBackFunction) {
            _errorCallBackFunction = errorCallBackFunction;
        }

        Request.put(`/hr/api/v1/adp-report/employee/update/${form.id}`, form).call((response) => {

            Response.of(response.status)
                .ok(_okCallBackFunction)
                .otherwise(_errorCallBackFunction).then(() => {

                    if(thenCallBackFunction) {
                        thenCallBackFunction();
                    }

                });

        });

    };

    svc.initialize = () => {

        // Init the configuration of the requests
        Request.config($http,$q);

    }

    // Init the service
    {
        svc.initialize();
    }

});


