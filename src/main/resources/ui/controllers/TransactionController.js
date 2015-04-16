'use strict';

var TransactionController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

    var data = [];

    $scope.createLabel = null
    $scope.createIcon = null
    var permissions = $rootScope.permissions
    for (var i in permissions){
        var p = permissions[i]
        console.log("Checking permission : "+ p.permission)
        if (p.permission == "createTrans"){
            console.log("Found")
            $scope.createLabel = p.label
            $scope.createIcon = p.style
        }
    }

    $http.get('/api/transaction/all').success(function (transactions) {

        data = transactions;

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
