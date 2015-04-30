'use strict';

var FinancialRatioController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

    $scope.currentRatio = 0.0
    $scope.quickRatio = 0.0
    $scope.inventoryToNetWorkingCapital = 0.0
    $scope.debtToAssetRatio =  0.0
    $scope.debtToEquityRatio = 0.0
    $scope.longTermDebtToEquityRatio = 0.0

    $scope.currentAssets = 0.0
    $scope.inventory = 0.0
    $scope.totalAssets = 0.0
    $scope.currentLiabilities = 0.0
    $scope.totalLiabilities = 0.0
    $scope.stakeHolderEquity = 0.0
    $scope.longTermLiabilities = 0.0

    $http.get('/report/financialratio/2015/4').success(function (data) {
        console.log('financialratio Data = '+JSON.stringify(data));

        $scope.currentRatio = data.currentRatio;
        $scope.quickRatio = data.quickRatio;
        $scope.inventoryToNetWorkingCapital = data.inventoryToNetWorkingCapital;
        $scope.debtToAssetRatio =  data.debtToAssetRatio ;
        $scope.debtToEquityRatio = data.debtToEquityRatio;
        $scope.longTermDebtToEquityRatio = data.longTermDebtToEquityRatio;

        $scope.currentAssets = data.currentAssets;
        $scope.inventory = data.inventory;
        $scope.totalAssets = data.totalAssets;
        $scope.currentLiabilities = data.currentLiabilities;
        $scope.totalLiabilities = data.totalLiabilities;
        $scope.stakeHolderEquity = data.stakeHolderEquity;
        $scope.longTermLiabilities = data.longTermLiabilities;
    });
};
