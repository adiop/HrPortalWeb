'user strict'

//Task controller
app.controller('portalController', function($scope, portalService) {

    const ctrl = this;
    const svc = portalService;

    ctrl.hasTaskPermission = (form) => {
        return (form.result && form.result.length > 0) || (form.user && form.user.isUserAdmin);
    }

    // Initialize
    {

        Common.initializeController(ctrl);

        // Reset form
        ctrl.form = PortalForm.newInstance();
        ctrl.form.reset();

        // Load the user
        svc.loadUser(ctrl.form);

        // Load the tasks
        svc.loadTasks(ctrl.form);

    }

});