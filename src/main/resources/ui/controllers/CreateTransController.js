'use strict';
function CreateTransController($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $rootScope.tForm = {}
    $rootScope.tForm.description = null
    $rootScope.tForm.entry =[{accountid:null, amount:null, debit:false}, {accountid:null, amount:null, debit:true}]

    $scope.errormessage = null;
    $rootScope.accounts = [];

    $scope.fetchOptions = function() {

        $http.get('/api/account/all').success(function(data){
            $rootScope.accounts = data;
        });
    };

    $scope.save = function(){
        console.log('Save Transaction');
        var trans = {description:$scope.tForm.description, entries:$scope.tForm.entry};

        console.log('Trans = '+JSON.stringify(trans))

        $http.post('api/transaction',trans)
            .success(function(data, status, headers, config){

                $location.path("/transaction")
            })
            .
            error(function(data, status, headers, config) {

                $scope.errormessage = data;
            });
    };

    $rootScope.addEntry = function(){
        $rootScope.tForm.entry.push({accountid:null, amount:null, debit:null})
    };

    $rootScope.removeEntry = function(index){
        $rootScope.tForm.entry.splice(index,1)
    };
    $scope.fetchOptions()
};
