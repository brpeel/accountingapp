'use strict';

var TransactionController = function( $scope, $http) {

    $scope.fetchTrans = function() {

        $http.get('/api/transaction/all').success(function(data){
            $scope.myData = data;
        });
    };

    $scope.fetchTrans();
};
