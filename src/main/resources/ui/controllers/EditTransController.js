'use strict';
function EditTransController($rootScope, $scope, $http, $window, $location, $routeParams) {

    var self = this;

    $rootScope.tForm = {}
    $rootScope.tForm.description = null
    $rootScope.tForm.entry =[{accountid:null, amount:null, debit:false}, {accountid:null, amount:null, debit:true}]

    $scope.errormessage = null;
    $rootScope.accounts = [];
    $rootScope.transaction = null;
    $rootScope.status = "Reported";
    $rootScope.showSave = true;
    $rootScope.showApprove = false;
    var canApprove = false;

    var permissions = $rootScope.permissions
    console.log(JSON.stringify(permissions))
    for (var i in permissions){
        var p = permissions[i]
        console.log("Checking permission : "+ p.permission)
        if (p.permission == "ApproveTrans"){
            console.log("Checking permission found "+ p.permission)
            canApprove = true
        }
    }

    $scope.fetchOptions = function() {
        $http.get('/api/account/all').success(function(data){
            $rootScope.accounts = data;
        });
    };

    $scope.fetchTrans = function() {
        var id = $routeParams.id

        console.log("Checking loading transaction : "+ id)

        if (typeof id == "undefined" || !id) {
            $location.path("/")
            return;
        }

        console.log('Fetching Transaction Id with id :'+id)
        $http.get('/api/transaction/'+id)
            .success(function(data){
                console.log('Trans = '+JSON.stringify(data))
                $rootScope.tForm.description = data.description
                $rootScope.tForm.entry = data.entries
                $rootScope.transaction = data.id;
                var status = data.status.toLocaleLowerCase();
                console.log("Got status : "+ status)
                if (status == "reported"){
                    console.log("Show save");
                    $rootScope.showSave = true;
                    $rootScope.showApprove = canApprove;
                }
                else {
                    $rootScope.showSave = false;
                    $rootScope.showApprove = false;
                }
            })
            .error(function(data, status, headers, config) {

                $scope.errormessage = data;
            });;
    };

    $scope.save = function(){
        console.log('Save Transaction');
        var trans = {description:$scope.tForm.description, entries:$scope.tForm.entry};
        var id = $rootScope.transaction
        $http.put('api/transaction/'+id,trans)
            .success(function(data, status, headers, config){

                $location.path("/transactions")
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

    $rootScope.approve = function(){
        console.log('Approve Transaction = '+$rootScope.transaction)

        var trans = {description:$scope.tForm.description, entries:$scope.tForm.entry};
        var id = $rootScope.transaction

        $http.put('api/transaction/approve?id='+id,trans)
            .success(function(data, status, headers, config){

                $location.path("/transactions")
            })
            .
            error(function(data, status, headers, config) {

                $scope.errormessage = data;
            });
    };

    $rootScope.reject = function(){
        console.log('Reject Transaction = '+$rootScope.transaction)
        var trans = {description:$scope.tForm.description, entries:$scope.tForm.entry};
        var id = $rootScope.transaction
        $http.put('api/transaction/reject?id='+id,trans)
            .success(function(data, status, headers, config){

                $location.path("/transactions")
            })
            .
            error(function(data, status, headers, config) {

                $scope.errormessage = data;
            });
    };

    $scope.fetchTrans()
    $scope.fetchOptions()
};
