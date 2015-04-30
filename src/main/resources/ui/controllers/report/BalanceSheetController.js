'use strict';

var BalanceSheetController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

    $scope.assets = [];
    $scope.liabilities = [];
    $scope.ownersEquity = [];

    $scope.totalAssets = 0.0;
    $scope.totalLiabilities = 0.0;
    $scope.ownerEquity = 0.0;
    $scope.ownersEquityPlusLiabilities = 0.0;

    $http.get('/report/balancesheet/2015/4').success(function (data) {
        console.log('Balance Sheet for Date = '+JSON.stringify(data));

        $scope.assets = data.assets;
        $scope.liabilities = data.liabilities;
        $scope.ownersEquity = data.ownerEquity;

        $scope.totalAssets = data.totalAssets;
        $scope.totalLiabilities = data.totalLiabilities;
        $scope.ownerEquity = data.ownerEquity;
        $scope.ownersEquityPlusLiabilities = data.ownersEquityPlusLiabilities;
        $scope.ownerCapitalAccount = data.ownerCapitalAccount;

    });

};
