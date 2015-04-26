'use strict';

var SearchTransactionController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

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
                data = response
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

    $scope.loadData = function() {

        data = [];
            [{
            "documents": [],
            "submitted": true,
            "id": 1,
            "reported_by": 3,
            "approved_by": 2,
            "reported": "04/16/2015 12:05:20",
            "approved": "04/16/2015 12:11:49",
            "status": "Approved",
            "description": "Some Desc",
            "entries": [],
            "accounts": "",
            "debits": "0.00",
            "credits": "0.00"
        }, {
            "documents": [],
            "submitted": false,
            "id": 2,
            "reported_by": 3,
            "approved_by": null,
            "reported": "04/16/2015 12:07:06",
            "approved": null,
            "status": "Rejected",
            "description": "Some Trans",
            "entries": [],
            "accounts": "",
            "debits": "0.00",
            "credits": "0.00"
        }];

        $scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 10,           // count per page
            sorting: {TransId: 'asc'}
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


        //data = [{"documents":[],"submitted":true,"id":1,"reported_by":3,"approved_by":2,"reported":"04/16/2015 12:05:20","approved":"04/16/2015 12:11:49","status":"Approved","description":"Some Desc","entries":[],"accounts":"","debits":"0.00","credits":"0.00"},{"documents":[],"submitted":false,"id":2,"reported_by":3,"approved_by":null,"reported":"04/16/2015 12:07:06","approved":null,"status":"Rejected","description":"Some Trans","entries":[],"accounts":"","debits":"0.00","credits":"0.00"},{"documents":[],"submitted":false,"id":3,"reported_by":2,"approved_by":null,"reported":"04/25/2015 05:57:56","approved":null,"status":"Reported","description":"Some new trans","entries":[],"accounts":"","debits":"0.00","credits":"0.00"},{"documents":[],"submitted":false,"id":106,"reported_by":2,"approved_by":null,"reported":"04/26/2015 12:48:44","approved":null,"status":"Reported","description":"SOme desc","entries":[],"accounts":"","debits":"0.00","credits":"0.00"}]
       /* $scope.tableData = data;

        $scope.tableParams.total(data.length);
        $scope.tableParams.reload();*/
    }


    $scope.updateTable = function(){
        data = dataFactory.query();
        data.$promise.then(function (data){
            $scope.tableParams.reload();
        });
    }

    $scope.loadData();

};
