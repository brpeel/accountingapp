'use strict';
function EditTransController($rootScope, $scope, $http, $window, $location, $routeParams, FileUploader, $filter, ngTableParams) {

    var id = $routeParams.id
    if (typeof id == "undefined" || !id) {
        $location.path("/")
        return;
    }

    var self = this;

    $scope.tForm = {}
    $scope.tForm.description = null
    $scope.tForm.status = "Reported";
    $scope.tForm.entry =[{accountid:null, amount:null, debit:false}, {accountid:null, amount:null, debit:true}]

    $scope.errormessage = null;
    $scope.accounts = [];
    $scope.transId = id;

    $scope.showSave = true;
    $scope.showApprove = false;
    $scope.canApprove = $rootScope.allowed.ApproveTrans;

    $scope.documents = [];
    $scope.alerts = [];

    $scope.token = $window.sessionStorage.token


    $scope.fetchOptions = function() {
        $http.get('/api/account/all').success(function(data){
            $scope.accounts = data;
        });
    };

    $scope.fetchTrans = function() {

        $http.get('/api/transaction/'+id)
            .success(function(response){

                console.log(JSON.stringify(response));
                $scope.tForm.description = response.description;
                $scope.tForm.entry = response.entries;
                $scope.tForm.status = response.status;

                var status = response.status.toLowerCase();

                $scope.showSave = status != "posted";
                $scope.showApprove = $scope.canApprove && status == "reported";

                console.log("showSave = "+$scope.showSave+" " )
            })
            .error(function(data, status, headers, config) {

                $scope.addAlert('danger', 'Could not load transaction: '+id);
            });;
    };

    $scope.fetchDocuments = function() {
        var id = $routeParams.id

        if (typeof id == "undefined" || !id) {
            $location.path("/")
            return;
        }

        $http.get('/api/transdocument/'+id)
            .success(function(data){
                $scope.documents = data;
            })
            .error(function(data, status, headers, config) {

                $scope.addAlert('danger', 'Could not load document list');
            });
    };

    $scope.save = function(){
        console.log('Save Transaction');
        var trans = {description:$scope.tForm.description, entries:$scope.tForm.entry};
        $http.put('api/transaction/update/'+id,trans)
            .success(function(data, status, headers, config){

                $location.path("/transactions")
            })
            .
            error(function(data, status, headers, config) {

                $scope.addAlert('danger', 'Could not save transaction');
                $scope.addAlert('danger', data);
            });
    };

    $scope.post = function(){
        console.log('Approve Transaction = '+id)

        var trans = {description:$scope.tForm.description, entries:$scope.tForm.entry};


        $http.put('api/transaction/post/'+id,trans)
            .success(function(data, status, headers, config){

                $location.path("/transactions")
            })
            .error(function(data, status, headers, config) {

                $scope.addAlert('danger', 'Could not Approve transaction');
                $scope.addAlert('danger', data);
            });
    };

    $scope.reject = function(){
        console.log('Reject Transaction = '+id)
        var trans = {description:$scope.tForm.description, entries:$scope.tForm.entry};

        $http.put('api/transaction/reject/'+id,trans)
            .success(function(data, status, headers, config){

                $location.path("/transactions")
            })
            .error(function(data, status, headers, config) {
                $scope.addAlert('danger', 'Could not reject transaction');
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

    var uploader = new FileUploader({
        url: 'api/transdocument/'+ $routeParams.id,
        headers: {'Authorization': $window.sessionStorage.token}
    });

    $scope.uploader = uploader
        // FILTERS

    uploader.filters.push({
        name: 'customFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 10;
        }
    });

    // CALLBACKS

    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);

        $scope.fetchDocuments();

    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
        $scope.addAlert('danger', 'Could not upload file');
    };

    $scope.addAlert = function(type, message) {
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };


    $scope.fetchTrans();
    $scope.fetchDocuments();
    $scope.fetchOptions();
};
