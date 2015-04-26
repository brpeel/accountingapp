'use strict';

var HomeController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {


    $scope.alerts = []

    $scope.addAlert = function(type, message) {
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    console.log($rootScope.allowed)
   // console.log($rootScope.permissions)
    $scope.fetch = function() {

        $http.get('/api/timeline')
            .success(function(data){
                $scope.timeline = data;
                console.log(JSON.stringify(data))
            })
            .error(function(response, status, headers, config) {

                $scope.addAlert('danger', 'An error occurred while searching for your transaction');
                if (response)
                    $scope.addAlert('danger', response);
        });
    };

    $scope.fetch();
};
