'use strict';

var TrialBalanceController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {


    $http.get('/report/trailbalance/2015/4').success(function (data) {
        console.log('trailbalance Data = '+JSON.stringify(data));

        $scope.accounts = data.accounts;
        $scope.date = data.date;
        $scope.totalDebit = data.totalDebit;
        $scope.totalCredit = data.totalCredit;
    });
};
