'use strict';

//Task service
app.service('taskService', function(config, $http, $q) {

    const svc = this;

    svc.isListAll = false;

    svc.resetErrors = (form) => {
        form.hasError = null;
        form.errorMessage = null;
    }

    svc.handleError = (form, message) => {
        form.hasError = true;
        form.errorMessage = message;
    }

    svc.loadTaskDetails = (task) => {

        task.loadingDetails = true;

        svc.resetErrors(task);

        Request.get(`/hr/api/v1/task/${task.id}/${svc.isListAll}`).call(response => {

            task.loadingDetails = false;

            Response.of(response.status)
                .ok(() => {
                    task.details = response.data;
                }).otherwise(() => {
                    svc.handleError(task, 'An error has occurred while trying to load the task details!');
                });

        });

    }

    svc.loadTaskDetailsQuitely = (task) => {

        svc.resetErrors(task);

        Request.get(`/hr/api/v1/task/${task.id}/${svc.isListAll}`).call(response => {

            Response.of(response.status)
                .ok(() => {
                    task.progressPercentage = response.data.progressPercentage;
                    task.details = response.data;
                }).otherwise(() => {
                    svc.handleError(task, 'An error has occurred while trying to update the task details!');
                });

        });

    }

    svc.list = (form) => {

        svc.resetErrors(form);
        let search = null;

        if(form.searchType === 'OPEN') {
            search = 'open';
        } else if(form.searchType === 'CLOSED') {
            search = 'closed';
        } else if(form.searchType === 'FUTURE') {
            search = 'future';
        }

        svc.isListAll = form.isListAll;

        let uri = `/hr/api/v1/task/list/${search}/${form.isListAll}`;

        if(form.name) {
            uri = uri + `?taskStaff.fullName=${form.name}`;
        }

        Request.get(uri).call((response) => {
            Response.of(response.status).ok(() => {
                form.result = response.data;
                form.result.searchType = search;
            }).otherwise(() => {
                svc.handleError(form, 'An error has occurred while trying to find the tasks!');
            });
        });


    };

    svc.insertComment = (task, comment) => {

        svc.resetErrors(task);
        task.details.isUpdating = true;

        Request.put(`/hr/api/v1/task/comment/${task.id}`, comment).call((insertCommentResponse) => {

            Response.of(insertCommentResponse.status).ok(() => {
                svc.loadTaskDetailsQuitely(task);
            }).otherwise(() => {
                task.details.isUpdating = false;
                svc.handleError(task, 'An error has occurred while trying to insert the comment!');
            });

        });

    }

    svc.reassignTicket = (task, ticket, technician, reason) => {

        svc.resetErrors(ticket);
        task.details.isUpdatingTicket = true;
        ticket.isUpdating = true;

        Request.put(`/hr/api/v1/task/ticket/reassign/${ticket.id}/${technician.id}`, reason).call((reassignTicketResponse) => {

            Response.of(reassignTicketResponse.status).ok(() => {
                svc.loadTaskDetailsQuitely(task);
            }).otherwise(() => {
                ticket.isUpdating = false;
                svc.handleError(ticket, 'An error has occurred while trying to reassign the ticket!');
            });

        });

    }

    svc.closeTicket = (task, ticket, resolution) => {

        svc.resetErrors(ticket);
        task.details.isUpdatingTicket = true;
        ticket.isUpdating = true;

        Request.put(`/hr/api/v1/ticket/close/${ticket.id}`, resolution).call((closeTicketResponse) => {

            Response.of(closeTicketResponse.status).ok(() => {
                svc.loadTaskDetailsQuitely(task);
            }).otherwise(() => {
                ticket.isUpdating = false;
                svc.handleError(ticket, 'An error has occurred while trying to close the ticket!');
            });

        });

    }

    svc.reopenTicket = (task, ticket, reason) => {

        svc.resetErrors(ticket);
        task.details.isUpdatingTicket = true;
        ticket.isUpdating = true;

        Request.put(`/hr/api/v1/ticket/reopen/${ticket.id}`, reason).call((reopenTicketResponse) => {

            Response.of(reopenTicketResponse.status).ok(() => {
                svc.loadTaskDetailsQuitely(task);
            }).otherwise(() => {
                ticket.isUpdating = false;
                svc.handleError(ticket, 'An error has occurred while trying to reopen the ticket!');
            });

        });

    }

    svc.loadADUSer = (staff, form) => {

        svc.resetErrors(form);
        form.dbStaff = staff;
        form.loadingAdUser = true;

        Request.get(`/hr/api/v1/task/active-directory/user/${staff.login}`).call((loadUserResponse) => {

            Response.of(loadUserResponse.status).ok(() => {

                form.loadingAdUser = false;

                if (loadUserResponse.data.login) {
                    form.activeDirectoryUser = loadUserResponse.data;
                }

            }).otherwise(() => {
                svc.handleError(form, 'An error has occurred while trying to load the AD user!');
            });

        });

    }

    svc.pushToAD = (form, fields) => {

        // Messages
        form.syncSuccessMessage = null;
        form.syncErrorMessage = null;

        let staff = form.dbStaff;

        if(staff != null && fields.length > 0) {

            form.loadingAdUser = true;

            Request.post(`/hr/api/v1/task/ad/push/${staff.id}`, fields).call((adSyncResponse) => {

                Response.of(adSyncResponse.status).ok(() => {
                    form.syncSuccessMessage = 'Employee Data synchronized with AD!'
                    svc.loadADUSer(staff, form)
                }).otherwise(() => {
                    form.syncErrorMessage = 'An error has occurred!'
                });

            });

        }


    }

    svc.pullFromAD =  (form, fields) => {

        // Messages
        form.syncSuccessMessage = null;
        form.syncErrorMessage = null;

        let staff = form.dbStaff;

        if(staff != null && fields.length > 0) {

            form.loadingDbStaff = true;

            Request.post(`/hr/api/v1/task/ad/pull/${staff.id}`, fields).call((adSyncResponse) => {

                Response.of(adSyncResponse.status).ok(() => {

                    form.syncSuccessMessage = 'Employee Data synchronized with AD!'

                    Request.get(`/hr/api/v1/staff/${staff.id}`).call((adDataResponse) => {

                        form.loadingDbStaff = false;

                        Response.of(adDataResponse.status).ok(() => {
                            form.dbStaff = adDataResponse.data;
                        });

                    });

                }).otherwise(() => {
                    form.syncErrorMessage = 'An error has occurred!'
                });

            });

        }

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