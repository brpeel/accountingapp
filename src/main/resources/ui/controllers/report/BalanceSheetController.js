'use strict';

var BalanceSheetController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

    $scope.assets = [];
    $scope.liabilities = [];
    $scope.ownersEquity = [];

    $http.get('/report/balancesheet/2015/4').success(function (data) {
        console.log('Balance Sheet for Date = '+JSON.stringify(data));

        $scope.assets = data.assets;
        $scope.liabilities = data.liabilities;
        $scope.ownersEquity = data.ownerEquity;


    });

};
