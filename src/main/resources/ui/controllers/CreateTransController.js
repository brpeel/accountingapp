'use strict';
function CreateTransController($rootScope, $scope, $http, $window, $location) {

    var self = this;

    $rootScope.tForm = {}
    $rootScope.tForm.description = null
    $rootScope.tForm.entry =[{accountid:null, amount:null, debit:false}, {accountid:null, amount:null, debit:true}]

    $scope.errormessage = null;
    $rootScope.accounts = [];
    $rootScope.transaction = null;
    $rootScope.status = "Reported";
    $rootScope.showSave = true;
    $rootScope.showSubmit = false;
    $rootScope.showApprove = false;

    $scope.fetchOptions = function() {
        $http.get('/api/account/all').success(function(data){
            $rootScope.accounts = data;
        });
    };

    $scope.fetchTrans = function() {
        var id = $location.search().id
        if (typeof id == "undefined" || !id)
            return;

        console.log('Fetching Transaction Id with id :'+id)
        $http.get('/api/transaction/'+id)
            .success(function(data){
                console.log('Trans = '+JSON.stringify(data))
                $rootScope.tForm.description = data.description
                $rootScope.tForm.entry = data.entries
                $rootScope.transaction = data.id;
                var status = data.status.toLocaleLowerCase();

                if (status == "reported"){
                    $rootScope.showSave = false;
                    $rootScope.showSubmit = true;
                    $rootScope.showApprove = false;
                }
                else if (status == "submitted"){
                    $rootScope.showSave = false;
                    $rootScope.showSubmit = false;
                    $rootScope.showApprove = true;
                }
                else {
                    $rootScope.showSave = false;
                    $rootScope.showSubmit = false;
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

    $rootScope.addEntry = function(){
        $rootScope.tForm.entry.push({accountid:null, amount:null, debit:null})
    };

    $rootScope.removeEntry = function(index){
        $rootScope.tForm.entry.splice(index,1)
    };

    $rootScope.submit = function(){
        console.log('Submit Transaction = '+$rootScope.transaction)
    };

    $rootScope.approve = function(){
        console.log('Approve Transaction = '+$rootScope.transaction)

    };

    $rootScope.reject = function(){
        console.log('Reject Transaction = '+$rootScope.transaction)

    };

    $scope.fetchTrans()
    $scope.fetchOptions()
};
