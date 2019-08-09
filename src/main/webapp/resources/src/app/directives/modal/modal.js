app.directive("modal", function() {
    return {
        restrict : "E",
        scope: {
            modalId: '=modalId',
            modalTitle: '=modalTitle',
            loading: '=?loading'
        },
        link: function($scope){
            $scope.loading = angular.isDefined($scope.loading) ? $scope.loading : false;
        },
        transclude: {
            'content': 'modalContent',
            'footer': '?modalFooter'
        },
        templateUrl : "/resources/src/app/directives/modal/template/modal.html"
    };
});

app.directive("modalDialog", function() {
    return {
        restrict : "E",
        transclude: true,
        scope: {
            modalId: '=modalId',
            onConfirm: '&onConfirm'
        },
        templateUrl : "/resources/src/app/directives/modal/template/modalDialog.html"
    };
});