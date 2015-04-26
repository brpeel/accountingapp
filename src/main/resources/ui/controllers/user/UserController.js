'use strict';

var UserController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

    var data = [];

    $http.get('/api/user/all/true').success(function (users) {

        data = users;

        $scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 10,           // count per page
            sorting: {
                UserName: 'asc'
            }
        }, {
            total: data.length, // length of data
            getData: function($defer, params) {
                // use build-in angular filter

                var orderedData = params.sorting() ?
                    $filter('orderBy')(data, params.orderBy()) :
                    data;

                orderedData = params.filter ?
                    $filter('filter')(orderedData, params.filter()) :
                    orderedData;

                $scope.users = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve($scope.users);
            }
        });
    });
};
