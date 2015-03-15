'use strict';

var ChartOfAccountsController = function($rootScope, $scope, $http, $window, $location) {

    $scope.createLabel = null
    $scope.createIcon = null
    var permissions = $rootScope.permissions
    for (var i in permissions){
        var p = permissions[i]
        console.log("Checking permission : "+ p.permission)
        if (p.permission == "createAccount"){
            console.log("Found")
            $scope.createLabel = p.label
            $scope.createIcon = p.style
        }
    }

    console.log($rootScope.permissions)
    $scope.fetch = function() {

        $http.get('/api/account/all').success(function(data){
            $scope.accounts = data;
        });
    };

    $scope.fetch();
};
