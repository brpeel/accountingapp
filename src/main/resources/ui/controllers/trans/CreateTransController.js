'use strict';
function CreateTransController($rootScope, $scope, $http, $window, $location, $routeParams) {

    var self = this;

    $scope.tForm = {}
    $scope.tForm.description = null
    $scope.tForm.entry =[{accountid:null, amount:null, debit:false}, {accountid:null, amount:null, debit:true}]

    $scope.alerts = [];
    $scope.accounts = [];
    $scope.transaction = null;
    
    $scope.fetchOptions = function() {
        $http.get('/api/account/all').success(function(data){
            $scope.accounts = data;
        });
    };

    $scope.save = function(){
        console.log('Save Transaction');

        var trans = {description:$scope.tForm.description, entries:$scope.tForm.entry};

        $http.post('api/transaction',trans)
            .success(function(data, status, headers, config){

                $location.path("/transactions")
            })
            .
            error(function(data, status, headers, config) {
                console.log(data)

                $scope.addAlert('danger', 'Could not save the transaction');
                $scope.addAlert('danger', data);
            });
    };

    $scope.cancel = function(){
        $location.path("/transactions");
    };

    $scope.addEntry = function(){
        $scope.tForm.entry.push({accountid:null, amount:null, debit:null})
    };

    $scope.removeEntry = function(index){
        $scope.tForm.entry.splice(index,1)
    };

    $scope.addAlert = function(type, message) {
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };


    $scope.fetchOptions()
};
