'use strict';
function AssignSurrogateController($rootScope, $scope, $http, $window, $location, $routeParams) {

    console.log(JSON.stringify("In Surrogate Controller"));

    var self = this;
    $scope.alerts = [];
    $rootScope.today = new Date();
    $rootScope.tomorrow = (new Date())+1;

    $rootScope.tForm = {}

    $rootScope.showSave = true;


    //createUser editUser
    var permissions = $rootScope.permissions
    for (var i in permissions){
        var p = permissions[i]
        if (p.permission == "AssignSurrogate"){
            $rootScope.showSave = true;
        }
    }

    $scope.fetchExistingSurrogates = function() {
        $http.get('/api/user/checksurrogate').success(function(data){

            console.log(JSON.stringify(data))
            data.forEach(function(obj) {
                $scope.addAlert('info', obj.username+" is already your surrogate manager from "+obj.start+" until "+obj.end);
            });

        });
    };

    $scope.fetchOptions = function() {
        $http.get('/api/user/surrogateableusers').success(function(data){
            $rootScope.users = data;
        });
    };

    $scope.cancel = function(){
        $location.path("/");
    };

    $scope.save = function(){
        console.log('Save Surrogate');

        var startString = $scope.start;
        var endString = $scope.end;
        var surrogate = {userid:$scope.userid, start:startString, end:endString};

        $http.post('api/user/assignsurrogate',surrogate)
            .success(function(data, status, headers, config){
                $scope.addAlert('success', 'Surrogate has been assigned');
            })
            .
            error(function(data, status, headers, config) {
                if (typeof data == "undefined" || !data || data == "")
                    $scope.addAlert('danger', "Could not assign surrogate manager. Please contact your administrator");
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

    $scope.fetchOptions();
    $scope.fetchExistingSurrogates();
};
