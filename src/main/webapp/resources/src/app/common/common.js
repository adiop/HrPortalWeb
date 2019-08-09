class Common {

    static mapToSelect(values, labelFunction, valueFunction) {

        let map = [];

        map.push({label: '', value: ''});

        if (values) {
            values.forEach(v => {
                map.push({label: labelFunction(v), value: valueFunction(v)});
            });
        }

        map.sort((a, b) => {

            let nameA = a.label.toUpperCase();
            let nameB = b.label.toUpperCase();

            if (nameA > nameB) {
                return 1;
            }
            if (nameA < nameB) {
                return -1;
            }

            return 0;

        })

        return map;

    }

    static initializeService(svc) {

    }

    static initializeController(ctrl) {

        ctrl.messages = {
            successMessage: '',
            errorMessage: ''
        }

        ctrl.cleanMessages = () => {
            ctrl.messages.successMessage = '';
            ctrl.messages.errorMessage = '';
        }

        ctrl.resetForms = (form, pageForm) => {
            //Reset the form

            if(form)
                form.reset();

            if(pageForm)
                pageForm.$setUntouched();

            ctrl.cleanMessages();

        }

        ctrl.doThenClearSearch = (func) => {

            try {
                func();
            } finally {
                ctrl.search.show();
            }

        }


    }

}