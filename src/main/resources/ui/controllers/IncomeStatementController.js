'use strict';

var IncomeStatementController = function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {

   $scope.totalRevenue = 0.0;
   $scope.totalExpense = 0.0;
   $scope.netIncome = 0.0;
   $scope.revenues = [];
   $scope.expenses = [];

    $http.get('/report/income/2015/4').success(function (data) {
        console.log('Income Statement Date = '+JSON.stringify(data));

        $scope.totalRevenue = data.totalRevenues;
        $scope.revenues = data.revenues;

        $scope.totalExpense = data.totalExpenses;
        $scope.expenses = data.expenses;

        $scope.netIncome = data.netIncome;
    });

};
