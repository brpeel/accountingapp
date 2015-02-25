	// create the module and name it accountingApp
	//var accountingApp = angular.module('accountingApp', ['ngRoute', 'accountingApp.about']);
	var accountingApp = angular.module('accountingApp', ['ngRoute']);

	// configure our routes
	accountingApp.config(function($routeProvider) {
		$routeProvider

			// route for the home page
			.when('/logon', {
				templateUrl : 'ui/templates/logon.html',
				controller  : 'LogonController'
			})
            .when('/', {
                templateUrl : 'ui/templates/home.html',
                controller  : 'HomeController'
            })

			// route for the about page
			.when('/about', {
				templateUrl : 'ui/templates/about.html',
				controller  : 'aboutController'
			})

	}).run( function($rootScope, $location, $window) {

        // register listener to watch route changes
        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            if ($window.sessionStorage.token == null) {
                // no logged user, we should be going to #login
                if (next.templateUrl == "ui/templates/login.html") {
                    console.log('Already going to logon')
                } else {
                    // not going to #login, we should redirect now
                    console.log('Routing to logon')
                    $location.path("/logon");
                }
            }
        });
    });

    accountingApp.controller('HomeController', function($scope, $http,  $window) {
        // create a message to display in our view
        console.log("IN the Home Controller")

    });

	accountingApp.controller('LogonController', function($rootScope, $scope, $http,  $window, $location) {
		// create a message to display in our view
        var controller = new LogonController($rootScope, $scope, $http,  $window, $location)
	});

	accountingApp.controller('aboutController', function($scope, $http) {

		var controller = new AboutController($scope, $http)

	});

    accountingApp.factory('httpRequestInterceptor', function ($q,  $window, $location) {
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
