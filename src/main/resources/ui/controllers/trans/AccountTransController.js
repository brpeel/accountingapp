'use strict';

var AccountTransController = function($rootScope, $scope, $http, $window, $location, $routeParams, $filter, ngTableParams) {

    var data = [];

    var id = $routeParams.id

    $http.get('/api/transaction/account/'+id).success(function (transactions) {

        console.log('Transactions = '+JSON.stringify(transactions));
        data = transactions;

        $scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 10
        }, {
            total: data.length, // length of data
            getData: function($defer, params) {
                // use build-in angular filter

                var orderedData = params.sorting() ?
                    $filter('orderBy')(data, params.orderBy()) :
                    data;

                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
    });
};
