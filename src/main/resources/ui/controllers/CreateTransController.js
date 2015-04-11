'use strict';
function CreateTransController($rootScope, $scope, $http, $window, $location, $routeParams) {

    var self = this;

    $rootScope.tForm = {}
    $rootScope.tForm.description = null
    $rootScope.tForm.entry =[{accountid:null, amount:null, debit:false}, {accountid:null, amount:null, debit:true}]

    $scope.errormessage = null;
    $rootScope.accounts = [];
    $rootScope.transaction = null;
    $rootScope.status = "Reported";
    var permissions = $rootScope.permissions
    for (var i in permissions){
        var p = permissions[i]
        console.log("Checking permission : "+ p.permission)
        if (p.permission == "createTrans"){
            console.log("Found")
            $rootScope.showSave = true
        }
    }

    $scope.fetchOptions = function() {
        $http.get('/api/account/all').success(function(data){
            $rootScope.accounts = data;
        });
    };

    $scope.save = function(){
        console.log('Save Transaction');

        var trans = {description:$scope.tForm.description, entries:$scope.tForm.entry};

        var id = $routeParams.id
        if (typeof id != "undefined" && id)
            trans.id = id

        console.log('Trans = '+JSON.stringify(trans))

        $http.post('api/transaction',trans)
            .success(function(data, status, headers, config){

                $location.path("/transactions")
            })
            .
            error(function(data, status, headers, config) {

                $scope.errormessage = data;
            });
    };


    $rootScope.cancel = function(){
        $location.path("/transactions")
    };


    $rootScope.addEntry = function(){
        $rootScope.tForm.entry.push({accountid:null, amount:null, debit:null})
    };

    $rootScope.removeEntry = function(index){
        $rootScope.tForm.entry.splice(index,1)
    };


    $scope.fetchOptions()
};
