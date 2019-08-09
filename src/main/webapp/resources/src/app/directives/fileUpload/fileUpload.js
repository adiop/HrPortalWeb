app.directive("fileUpload", function() {
    return {
        require: "ngModel",
        link: function postLink(scope,elem,attrs,ngModel) {
            elem.on("change", function(e) {
                ngModel.$setViewValue(e.target.files[0]);
            })
        }
    }
});