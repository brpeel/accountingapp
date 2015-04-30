'use strict';

var AccountTransController = function($rootScope, $scope, $http, $window, $location, $routeParams, $filter, ngTableParams) {

    var data = [];

    var id = $routeParams.id

    $scope.alerts = [];





         $http.get('/api/account/' + id).success(function (account) {
             console.log("Account : "+JSON.stringify(account))
             $scope.active = account.active;
             console.log("Active : "+JSON.stringify($scope.active))
         });

         $http.get('/api/transaction/account/' + id).success(function (transactions) {

            console.log('Transactions = ' + JSON.stringify(transactions));
            data = transactions;

            $scope.tableParams = new ngTableParams({
                page: 1,            // show first page
                count: 10
            }, {
                total: data.length, // length of data
                getData: function ($defer, params) {
                    // use build-in angular filter

                    var orderedData = params.sorting() ?
                        $filter('orderBy')(data, params.orderBy()) :
                        data;

                    $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });
        });

    $scope.deactivate = function(){
        console.log('Deactivate Account');
        $scope.alerts = [];
        $http.put('api/account/deactivate/'+id)
            .success(function(data, status, headers, config){

                $scope.reload();
            })
            .
            error(function(data, status, headers, config) {

                $scope.addAlert('danger', 'Could not save transaction');
                $scope.addAlert('danger', data);
            });
    };


    $scope.activate = function(){
        console.log('Activate Account');
        $scope.alerts = [];
        $http.put('api/account/activate/'+id)
            .success(function(data, status, headers, config){

                $scope.reload();
            })
            .
            error(function(data, status, headers, config) {

                $scope.addAlert('danger', 'Could not save transaction');
                $scope.addAlert('danger', data);
            });
    };

    $scope.reload = function() {
        $window.location.reload();
    };


    $scope.addAlert = function(type, message) {
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

};
