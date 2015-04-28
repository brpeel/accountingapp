'use strict';
function CreateAccountControler($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $scope.cancel = function(){
        $location.path("/transactions");
    };

    $scope.save = function(){
        console.log('Create Account');
        var data = {
            'name':$scope.name,
            'initialBalance':$scope.initialBalance,
            'normalSide':$scope.side,
            'subcategory':$scope.category
        };

        $http.post('api/account',data)
            .success(function(data, status, headers, config){

                console.log(JSON.stringify(data))
                $location.path("/account")
            })
            .
            error(function(data, status, headers, config) {
                console.log(JSON.stringify(data))
                alert("Could not save Account");
            });
    }
};
//name, initial_balance, normal_side, subcategory