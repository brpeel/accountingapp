'use strict';

var ChartOfAccountsController = function( $scope, $http) {

    $scope.fetch = function() {

        $http.get('/api/account/all').success(function(data){
            $scope.accounts = data;
        });
    };

    $scope.fetch();
};
