class Form {

    constructor() {
        this.loading = false;
        this.result = [];
        this.form = {};
    }

}

class Request {

    static config(_http, _q) {
        Request.http = _http;
        Request.q = _q;
    }

    static post(url, data) {
        return Request.of({method: 'POST', url: url, data: data});
    }

    static put(url, data) {
        return Request.of({method: 'PUT', url: url, data: data});
    }

    static delete(url, data) {
        return Request.of({method: 'DELETE', url: url, data: data});
    }

    static get(url, params) {
        return Request.of({method: 'GET', url: url, params: params});
    }

    static upload(url, file) {

        return Request.of({method: 'POST', url: url, headers: {'Content-Type': undefined}, data: {file: file}, transformRequest: function (data, headersGetter) {

            var formData = new FormData();

            angular.forEach(data, function (value, key) {
                formData.append(key, value);
            });

            return formData;

        }});
    }

    static of(requestConfig) {
        return new Request(requestConfig);
    }

    constructor(requestConfig) {
        this.requestConfig = requestConfig;
    }

    call(callBackBackFunction) {
        this.internalCall().then((internalResult) => {
            callBackBackFunction(internalResult);
        });
    }

    internalCall() {

        let deferred = Request.q.defer();

        Request.http(this.requestConfig)
            .then((response) => {
                deferred.resolve(response);
            }, (response) => {
                deferred.resolve(response);
            });

        return deferred.promise;

    }

}

class Response {

    static of(responseStatus) {
        return new Response(responseStatus);
    }

    constructor(responseStatus) {
        this.responseStatus = responseStatus;
        this.found = false;
    }

    ok(callBackFunction) {

        if(this.responseStatus == 200) {
            callBackFunction();
            this.found = true;
        }

        return this;

    }

    create(callBackFunction) {

        if(this.responseStatus == 201) {
            callBackFunction();
            this.found = true;
        }

        return this;

    }

    unprocessableEntity(callBackFunction) {

        if(this.responseStatus == 422) {
            callBackFunction();
            this.found = true;
        }

        return this;

    }

    forbidden(callBackFunction) {

        if(this.responseStatus == 403) {
            callBackFunction();
            this.found = true;
        }

        return this;

    }

    otherwise(functionOtherwise) {

        if(!this.found) {
            functionOtherwise(this.value);
            this.found = true;
        }

        return this;

    }

    then(thenFunction) {

        if(this.found) {
            thenFunction(this.value);
        }

    }

}

class Case {

    static of(value) {
        return new Case(value);
    }

    constructor(value) {
        this.value = value;
        this.found = false;
    }

    when(valueWhen, functionWhen) {

        if(!this.found) {

            if(this.value == valueWhen) {
                functionWhen(this.value);
                this.found = true;
            }

        }

        return this;

    }

    otherwise(functionOtherwise) {

        if(!this.found) {
            functionOtherwise(this.value);
        }

    }


}