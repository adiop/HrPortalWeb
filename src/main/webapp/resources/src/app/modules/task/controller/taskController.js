'user strict'

//Task controller
app.controller('taskController', function($scope, taskService, userService) {

    const ctrl = this;
    const svc = taskService;

    // Task
    {

        ctrl.loadTaskDetails = (loadTask, task) => {

            if(loadTask)
                svc.loadTaskDetails(task);

        }

        ctrl.insertComment = (comment) => {
            let task = ctrl.selectedTask;
            svc.insertComment(task, comment);
        }

    }

    // Ticket
    {

        ctrl.filterTickets = (task, ticketTask) => {

            let isShow = true;

            if(task.technicianFilter && task.technicianFilter != '') {
                isShow &= ticketTask.technician.fullName.toUpperCase().indexOf(task.technicianFilter.toUpperCase()) != -1;
            }

            if(task.descriptionFilter && task.descriptionFilter != '') {
                isShow &= ticketTask.description.toUpperCase().indexOf(task.descriptionFilter.toUpperCase()) != -1;
            }

            if(task.statusFilter && task.statusFilter != '') {

                let taskStatus = null;

                if('PENDING'.toUpperCase().indexOf(task.statusFilter.toUpperCase()) != -1) {
                    taskStatus = 'OPEN';
                } else if('CLOSED'.toUpperCase().indexOf(task.statusFilter.toUpperCase()) != -1) {
                    taskStatus = 'CLOSED';
                } else {
                    taskStatus = '';
                }

                isShow &= ticketTask.status === taskStatus;
            }

            return isShow;

        }

        // Select technician
        ctrl.selectTechnician = (form, technician) => {
            form.technician = technician;
        }

        // Close ticket
        ctrl.closeTicket = (resolution) => {
            let task = ctrl.selectedTask;
            let ticketTask = ctrl.selectedTicketTask;
            svc.closeTicket(task, ticketTask, resolution);
        }

        // Reopen ticket
        ctrl.reopenTicket = (reason) => {
            let task = ctrl.selectedTask;
            let ticketTask = ctrl.selectedTicketTask;
            svc.reopenTicket(task, ticketTask, reason);
        }

        // Reassign ticket
        ctrl.reassignTicket = (technician, reason) => {
            let task = ctrl.selectedTask;
            let ticketTask = ctrl.selectedTicketTask;
            svc.reassignTicket(task, ticketTask, technician, reason);
        }

    }

    // Active Directory Sync
    {

        ctrl.adSync = {
            pushFields: [],
            pullFields : []
        }

        ctrl.adSync.reset = () => {

            ctrl.adSync.dbStaff = null;
            ctrl.adSync.activeDirectoryUser = null;

            while(ctrl.adSync.pushFields .length > 0)
                ctrl.adSync.pushFields .pop();

            while(ctrl.adSync.pullFields .length > 0)
                ctrl.adSync.pullFields .pop();

        }

        ctrl.adSync.load = (staff) => {
            ctrl.adSync.reset();
            svc.loadADUSer(staff, ctrl.adSync);
        }

        ctrl.adSync.push = () => {
            svc.pushToAD(ctrl.adSync, ctrl.adSync.pushFields.filter(f => f != undefined && f != ''));
        }

        ctrl.adSync.pull = () => {
            svc.pullFromAD(ctrl.adSync, ctrl.adSync.pullFields.filter(f => f != undefined && f != ''));
        }

    }

    ctrl.selectTechnician = (task, technician) => {
        ctrl.technician = technician;
    }

    /**
     * Search
     */
    ctrl.search = {
        form: TaskForm.newInstance()
    }

    ctrl.search.do = () => {
        taskService.list(ctrl.search.form);
    }

    ctrl.search.orderByStaffName = () => {
        ctrl.search.form.sortByStringProperty('taskStaff.preferredName');
    }

    ctrl.search.orderByCreatedByName = () => {
        ctrl.search.form.sortByStringProperty('createdBy.preferredName');
    }

    ctrl.search.orderByCreatedDate = () => {
        ctrl.search.form.sortByStringProperty('createdDate');
    }

    ctrl.search.orderByEffectiveDate = () => {
        ctrl.search.form.sortByStringProperty('effectiveDate');
    }

    // Initialize
    {
        Common.initializeController(ctrl);
        ctrl.selectedTask = null;
        ctrl.selectedTicketTask = null;

        // Load the logged user in the control
        userService.loadUser(ctrl);

    }

});
