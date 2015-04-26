'use strict';

var TransactionController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

    $scope.alerts = []

    $scope.addAlert = function(type, message) {
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };


    var data = [];

    $scope.queryData = function(searchTerms){
        console.log("Searching for transactions using "+JSON.stringify(searchTerms))
        var result = []
        $http.post('api/transaction/search',searchTerms)
            .success(function(response, status, headers, config){

                data = response;
                $scope.tableData = data;

                $scope.tableParams.total(data.length);
                $scope.tableParams.reload();
            })
            .
            error(function(response, status, headers, config) {
                console.log(response)

                $scope.addAlert('danger', 'An error occurred while searching for your transaction');
                if (response)
                    $scope.addAlert('danger', response);
            });

        return result;
    }

    $scope.initTable = function() {

        data = [];

        $scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 10//,           // count per page
            //sorting: {TransId: 'asc'}
        }, {
            total: data.length, // length of data
            getData: function ($defer, params) {
                // use build-in angular filter

                var orderedData = params.sorting() ?
                    $filter('orderBy')(data, params.orderBy()) :
                    data;

                orderedData = params.filter ?
                    $filter('filter')(orderedData, params.filter()) :
                    orderedData;

                $scope.transactions = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve($scope.transactions);
            }
        });
    }

    $scope.search = function(){

        var searchTerms = {
            'id':$scope.id,
            'keyword':$scope.keyword,
            'startDate':$scope.startDate,
            'endDate':$scope.endDate
        };
        $scope.queryData(searchTerms);

    }


    $scope.updateTable = function(){
        data = dataFactory.query();
        data.$promise.then(function (data){
            $scope.tableParams.reload();
        });
    }

    $scope.initTable();
    $scope.search({});

}
