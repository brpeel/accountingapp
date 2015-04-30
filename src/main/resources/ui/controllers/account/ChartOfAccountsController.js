'use strict';

var ChartOfAccountsController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

    var data = [];

    console.log($rootScope.allowed)
   // console.log($rootScope.permissions)
    $scope.fetch = function() {

        $http.get('/api/account/all/').success(function(accounts){
            //$scope.accounts = data;
            console.log(JSON.stringify(accounts))
            data = accounts;

            $scope.tableParams = new ngTableParams({
                page: 1,            // show first page
                count: 10,           // count per page
                sorting: {
                    Status: 'asc'
                }
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

    $scope.fetch();
};
