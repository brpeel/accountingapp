'use strict';

var TransactionController = function($rootScope, $scope, $http, $window, $location) {


    $scope.createLabel = null;
    $scope.createIcon = null;
    var permissions = $rootScope.permissions;
    for (var i in permissions){
        var p = permissions[i]
        if (p.permission == "createTrans"){
            $scope.createLabel = p.label
            $scope.createIcon = p.style
        }
    };

    $scope.fetchTrans = function() {

    };
    $http.get('/api/transaction/all').success(function(data){
        var grid_data = []

        for (var i in data) {
            var trans = data[i]
            console.log('Trans = '+JSON.stringify(trans))
            var credits = 0.0
            var debits = 0.0
            var accounts = ""

            for (var j in trans.entries){
                var entry = trans.entries[j]
                console.log('Entry = '+JSON.stringify(entry))

                if (entry.debit)
                    debits += entry.amount
                else
                    credits += entry.amount

                if (accounts == "")
                    accounts = entry.accountid
                else
                    accounts = ", "+entry.accountid
            }


            grid_data.push({TransId: trans.id,
                Description: trans.description,
                Reported: trans.reported,
                Status: trans.status,
                TotalCredits: credits,
                TotalDedits: debits,
                ApprovedBy: (trans.approved_by == 0 ? null : trans.approved_by),
                Approved: trans.approved,
                Accounts: accounts
            })
        }

        $scope.myData = grid_data;
    });

    $scope.fetchTrans();
};
