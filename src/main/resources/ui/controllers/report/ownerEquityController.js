'use strict';

var OwnerEquityController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

   $scope.startingBalance = 0.0;
   $scope.investments = 0.0;
   $scope.withdrawals = 0.0;
   $scope.netIncome = 0.0;
   $scope.incomePlusInvestments = 0.0;
   $scope.endingBalance = 0.0;
    $scope.accountName = "";
    $scope.startDate = "";
    $scope.endDate = "";

    $http.get('/report/ownerequity/2015/4').success(function (data) {
        console.log('Owner Equity Statement for Date = '+JSON.stringify(data));

        $scope.startingBalance = data.startingBalance;
        $scope.investments = data.investments;
        $scope.withdrawals = data.withdrawals;
        $scope.netIncome = data.netIncome;
        $scope.incomePlusInvestments = data.incomePlusInvestments;
        $scope.endingBalance = data.endingBalance;
        $scope.accountName = data.accountName;
        $scope.startDate = data.startDate;
        $scope.endDate = data.endDate;

        $scope.periodEnd = data.periodEnd;

    });

};
