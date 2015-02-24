	// create the module and name it accountingApp
	//var accountingApp = angular.module('accountingApp', ['ngRoute', 'accountingApp.about']);
	var accountingApp = angular.module('accountingApp', ['ngRoute']);

	// configure our routes
	accountingApp.config(function($routeProvider) {
		$routeProvider

			// route for the home page
			.when('/', {
				templateUrl : 'ui/templates/logon.html',
				controller  : 'LogonController'
			})

			// route for the about page
			.when('/about', {
				templateUrl : 'ui/templates/about.html',
				controller  : 'aboutController'
			})

	});
    var showLogon = false;
	// create the controller and inject Angular's $scope
	accountingApp.controller('LogonController', function($scope, $http) {
		// create a message to display in our view
        var controller = new LogonController($scope, $http)
	});

	accountingApp.controller('aboutController', function($scope, $http) {

		var controller = new AboutController($scope, $http)

	});

    accountingApp.factory('httpRequestInterceptor', function ($q) {
        return {
            request: function (config) {
                //var token = $cookieStore.get("auth");
                console.log('intercepted '+config.url);

                config.headers['Authorization'] = 'Brett';

                console.log('headers : '+JSON.stringify(config.headers));

                return config;
            },

            response: function(response){
                console.log('intercepted success response '+JSON.stringify(response));
                return response;
            },

            responseError: function(rejection){
                console.log('intercepted errored response '+JSON.stringify(rejection));
                showLogon = true;
                return $q.reject(rejection);
            }
        };
    });

    accountingApp.config(function ($httpProvider) {
        $httpProvider.interceptors.push('httpRequestInterceptor');
    });
/*
    accountingApp.directive('igLogin', function () {
        return {
            restrict: 'E',
            replace: true,
            template: '<p>Username:</p>',
            controller: function ($scope) {

                $scope.submit = function() {
                    $scope.login();
                    $("#loginModal").modal('hide');
                };

                $scope.cancel = function() {
                    $scope.loggingIn = false;
                    $("#loginModal").modal('hide');
                };

                $scope.$watch('loggingIn', function() {
                    if ($scope.loggingIn) {
                        $("#loginModal").modal('show');
                    };
                });
            }
        };
    });
    */
    accountingApp.directive("modalShow", function ($parse) {
        return {
            restrict: "A",
            link: function (scope, element, attrs) {

                //Hide or show the modal
                scope.showModal = function (visible, elem) {
                    if (!elem)
                        elem = element;

                    if (visible)
                        $(elem).modal("show");
                    else
                        $(elem).modal("hide");
                }

                //Watch for changes to the modal-visible attribute
                scope.$watch(attrs.modalShow, function (newValue, oldValue) {
                    scope.showModal(newValue, attrs.$$element);
                });

                //Update the visible value when the dialog is closed through UI actions (Ok, cancel, etc.)
                $(element).bind("hide.bs.modal", function () {
                    $parse(attrs.modalShow).assign(scope, false);
                    if (!scope.$$phase && !scope.$root.$$phase)
                        scope.$apply();
                });
            }

        };
    });



