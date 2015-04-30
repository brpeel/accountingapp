'use strict';
function EmailDocumentController($rootScope, $scope, $http, $window, $location, $routeParams) {

    var self = this;

    $scope.alerts = [];
    $scope.users= [];
    $scope.disable = false;

    var documentId = $routeParams.id
    if (typeof documentId == "undefined" || !documentId) {
        $location.path("/")
        return;
    }

    $scope.fetchUsers = function(){
        $http.get('/api/user/all').success(function(data){

            $scope.users = data;
        });
    };

    $scope.fetchDocument = function(){
        $http.get('/api/transdocument/data/'+documentId).success(function(data){
            console.log("Doc = "+JSON.stringify(data))
            $scope.attachementname = data.file_name;
        });
    };

    $scope.cancel = function(){
        $location.path("/users");
    };

    $scope.send = function(){
        $scope.alerts = [];

        $scope.addAlert('warning', 'Attempting to send the email. This could take several seconds');

        $scope.disable = true;

        var to = $scope.userid;
        var email = {
            subject:$scope.subject,
            body:$scope.body,
            attachment:documentId};

        $http.post('api/email/senddocument/'+to,email)
            .success(function(data, status, headers, config){
                $scope.disable = false;
                $scope.alerts = [];
                $scope.addAlert('success', 'Email has been sent!');
            })
            .
            error(function(data, status, headers, config) {
                    $scope.disable = false;
                $scope.alerts = [];
                if (typeof data == "undefined" || !data || data == "")
                    $scope.addAlert('danger', "Could not email user");
                else
                    $scope.addAlert('danger', data);
            });
    };

    $scope.addAlert = function(type, message) {
        console.log("Adding alert "+type+" with message "+message);
        $scope.alerts.push({type: type, msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.fetchUsers();
    $scope.fetchDocument();
};
