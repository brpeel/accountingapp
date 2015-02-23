	// create the module and name it accountingApp
	//var accountingApp = angular.module('accountingApp', ['ngRoute', 'accountingApp.about']);
	var accountingApp = angular.module('accountingApp', ['ngRoute']);

	// configure our routes
	accountingApp.config(function($routeProvider) {
		$routeProvider

			// route for the home page
			.when('/', {
				templateUrl : 'templates/home.html',
				controller  : 'mainController'
			})

			// route for the about page
			.when('/about', {
				templateUrl : 'templates/about.html',
				controller  : 'aboutController'
			})

			.when('/logon', {
                templateUrl : 'templates/logon.html',
                controller  : 'logonController'
            })

	});

	// create the controller and inject Angular's $scope
	accountingApp.controller('mainController', function($scope) {


	});

	accountingApp.controller('aboutController', function($scope, $http) {

		var controller = new AboutController($scope, $http)

	});


	accountingApp.controller('LogonController', function($scope, $http) {
        var controller =  new LogonController($scope, $http)

	});
