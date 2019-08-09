'use strict';

class AdpReportForm extends Form {

    static newInstance() {
        return new AdpReportForm();
    }

    constructor() {
        super();
    }

    reset() {
        this.adpFile = null;
    }

}