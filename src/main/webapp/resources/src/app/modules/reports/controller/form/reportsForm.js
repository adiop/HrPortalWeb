'use strict';

class ReportsForm extends Form {

    static newInstance() {
        return new ReportsForm();
    }

    constructor() {
        super();
        this.reset();
    }

    reset() {

        // Initial date - 30 days ago
        this.initialDate = new Date();
        this.initialDate.setDate(this.initialDate.getDate() - 30);

        // Final date - today
        this.finalDate = new Date();

        this.lineChartBy = {label: 'Date', value: 'Date'};

    }

}