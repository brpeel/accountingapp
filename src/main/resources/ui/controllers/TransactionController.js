'use strict';

var TransactionController = function($rootScope, $scope, $http, $window, $location) {


    $scope.createLabel = null;
    $scope.createIcon = null;
    var permissions = $rootScope.permissions;
    for (var i in permissions){
        var p = permissions[i]
        if (p.permission == "createTrans"){
            $scope.createLabel = p.label
            $scope.createIcon = p.style
        }
    };

    $scope.fetchTrans = function() {

        $http.get('/api/transaction/all').success(function(data){
            $scope.myData = data;
        });
    };

    $scope.fetchTrans();
};
