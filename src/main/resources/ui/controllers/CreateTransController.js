'use strict';
function CreateTransController($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $scope.selectedAccount = null;
    $rootScope.accounts = [];

    $scope.fetchOptions = function() {

        $http.get('/api/account/all').success(function(data){
            console.log(JSON.stringify(data))
            $rootScope.accounts = data;
        });
    };

    $scope.save = function(){
        console.log('Reset Password');
        var trans = {'description':$scope.description};

        $http.post('api/transaction',trans)
            .success(function(data, status, headers, config){

                console.log(JSON.stringify(data))
                $location.path("/transaction")
            })
            .
            error(function(data, status, headers, config) {
                console.log(JSON.stringify(data))
                alert("Could not save transaction");
            });
    }

    $scope.fetchOptions()
};

/*
*
* $scope.selectedTestAccount = null;
 $scope.testAccounts = [];

 $http({
 method: 'GET',
 url: '/Admin/GetTestAccounts',
 data: { applicationId: 3 }
 }).success(function (result) {
 $scope.testAccounts = result;
 });
*
* */