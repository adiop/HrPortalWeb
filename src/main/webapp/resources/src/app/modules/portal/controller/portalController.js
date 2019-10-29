'user strict'

//Task controller
app.controller('portalController', function($scope, portalService, userService) {

    const ctrl = this;
    const svc = portalService;

    ctrl.hasTaskPermission = (form) => {
        return (form.result && form.result.length > 0) || (ctrl.user && ctrl.user.isUserAdmin);
    }

    // Initialize
    {

        Common.initializeController(ctrl);

        // Reset form
        ctrl.form = PortalForm.newInstance();
        ctrl.form.reset();

        // Load the logged user in the control
        userService.loadUser(ctrl);

        // Load the tasks
        svc.loadTasks(ctrl.form);

    }

});
