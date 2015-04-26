	// create the module and name it accountingApp
	//var accountingApp = angular.module('accountingApp', ['ngRoute', 'accountingApp.about']);
	var accountingApp = angular.module('accountingApp', ['ngRoute', 'ui.grid', 'ngTable', 'angularFileUpload']);

	// configure our routes
	accountingApp.config(function($routeProvider) {
		$routeProvider

			// User and Auth
			.when('/logon', {
				templateUrl : 'ui/templates/logon.html',
				controller  : 'LogonController'
			})
            .when('/logout', {
                templateUrl : 'ui/templates/logon.html',
                controller  : 'LogOutController'
            })
            .when('/resetpassword', {
                templateUrl : 'ui/templates/resetPassword.html',
                controller  : 'ResetPasswordController'
            })

            //Transactions
            .when('/transactions', {
                templateUrl : 'ui/templates/transactions.html',
                controller  : 'transactionController'
            })
            .when('/findTransaction', {
                templateUrl : 'ui/templates/searchTransactions.html',
                controller  : 'searchTransactionController'
            })
            .when('/transaction/:id', {
                templateUrl : 'ui/templates/editTransactions.html',
                controller  : 'editTransController'
            })
            .when('/createTrans', {
                templateUrl : 'ui/templates/createTransactions.html',
                controller  : 'createTransController'
            })
            .when('/accounttrans/:id', {
                templateUrl : 'ui/templates/accounttrans.html',
                controller  : 'accountTransController'
            })
            //Accounts
            .when('/accounts', {
                templateUrl : 'ui/templates/chartofaccounts.html',
                controller  : 'chartOfAccountsController'
            })
            .when('/createAccount', {
                templateUrl : 'ui/templates/createAccount.html',
                controller  : 'createAccountController'
            })

            //Users
            .when('/AssignSurrogate', {
                templateUrl : 'ui/templates/assignSurrogate.html',
                controller  : 'assignSurrogateController'
            })
            .when('/users', {
                templateUrl : 'ui/templates/user/users.html',
                controller  : 'userController'
            })
            .when('/user/:id', {
                templateUrl : 'ui/templates/user/editUser.html',
                controller  : 'editUserController'
            })
            .when('/createUser', {
                templateUrl : 'ui/templates/user/editUser.html',
                controller  : 'createUserController'
            })
            .when('/myuser/:id', {
                templateUrl : 'ui/templates/user/myuser.html',
                controller  : 'myUserController'
            })

            //reports
            .when('/incomeStatement',{
                templateUrl : 'ui/templates/report/incomeStatement.html',
                controller  : 'incomeStatementController'
            })

            .when('/ownersEquity',{
                templateUrl : 'ui/templates/report/ownerEquity.html',
                controller  : 'ownerEquityController'
            })
            .when('/balanceSheet',{
                templateUrl : 'ui/templates/report/balanceSheet.html',
                controller  : 'balanceSheetController'
            })
            .when('/financialRatio',{
                templateUrl : 'ui/templates/report/financialRatio.html',
                controller  : 'financialRatioController'
            })
            .when('/trialBalance',{
                templateUrl : 'ui/templates/report/trialBalance.html',
                controller  : 'trialBalanceController'
            })

            // route for the about page
            .when('/about', {
                templateUrl : 'ui/templates/about.html',
                controller  : 'aboutController'
            })
            .when('/', {
                templateUrl : 'ui/templates/home.html',
                controller  : 'HomeController'
            })
            .otherwise({
                redirectTo: '/'
            }
    );
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

    accountingApp.controller('HomeController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view

        var controller = new HomeController($rootScope, $scope, $http,  $window, $location)
    });
    accountingApp.controller('MenuController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        console.log("In the Menu Controller")
        var controller = new MenuController($rootScope, $scope, $http,  $window, $location)
    });

    accountingApp.controller('LogOutController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        console.log("clearing session")
        $rootScope.menuItems = []
        $rootScope.username = null
        $window.sessionStorage.clear()

        $http.post('auth/logout',null)
        $location.path("/logon")
    });

	accountingApp.controller('LogonController', function($rootScope, $scope, $http,  $window, $location) {
		// create a message to display in our view
        console.log('In LogonController')
        var controller = new LogonController($rootScope, $scope, $http,  $window, $location)
	});

    accountingApp.controller('ResetPasswordController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        console.log('In Reset Password Controller')
        var controller = new ResetPasswordController($rootScope, $scope, $http,  $window, $location)
    })

	accountingApp.controller('aboutController', function($scope, $http) {

		var controller = new AboutController($scope, $http)

	});

    //################################### Accounts and Transactions ####################################

    accountingApp.controller('transactionController', function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {
        var controller = new TransactionController($rootScope, $scope, $http,  $window, $location, $filter, ngTableParams)

    });

    accountingApp.controller('searchTransactionController', function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {
        var controller = new SearchTransactionController($rootScope, $scope, $http,  $window, $location, $filter, ngTableParams)

    });

    accountingApp.controller('editTransController', function($rootScope, $scope, $http,  $window, $location, $routeParams, FileUploader, $filter, ngTableParams) {
        // create a message to display in our view
        console.log('In Edit transaction controller')
        var controller = new EditTransController($rootScope, $scope, $http,  $window, $location, $routeParams, FileUploader, $filter, ngTableParams)
    });

    accountingApp.controller('createTransController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        var controller = new CreateTransController($rootScope, $scope, $http,  $window, $location)
    });

    accountingApp.controller('accountTransController', function($rootScope, $scope, $http,  $window, $location, $routeParams, $filter, ngTableParams) {
        // create a message to display in our view
        console.log('In Account transaction controller')
        var controller = new AccountTransController($rootScope, $scope, $http,  $window, $location, $routeParams, $filter, ngTableParams)
    });

    accountingApp.controller('chartOfAccountsController', function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {
        var controller = new ChartOfAccountsController($rootScope, $scope, $http, $window, $location, $filter, ngTableParams)

    });

    accountingApp.controller('createAccountController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        var controller = new CreateAccountControler($rootScope, $scope, $http,  $window, $location)
    });

    //################################### USERS ####################################

    accountingApp.controller('assignSurrogateController', function($rootScope, $scope, $http,  $window, $location) {

        var controller = new AssignSurrogateController($rootScope, $scope, $http,  $window, $location)
    });

    accountingApp.controller('userController', function($rootScope, $scope, $http, $window, $location, $filter, ngTableParams) {
        var controller = new UserController($rootScope, $scope, $http, $window, $location, $filter, ngTableParams)

    });

    accountingApp.controller('editUserController', function($rootScope, $scope, $http,  $window, $location, $routeParams) {

        console.log('In Edit User controller')
        var controller = new EditUserController($rootScope, $scope, $http,  $window, $location, $routeParams)

    });

    accountingApp.controller('myUserController', function($rootScope, $scope, $http,  $window, $location, $routeParams) {

        var controller = new MyUserController($rootScope, $scope, $http,  $window, $location, $routeParams)

    });

    accountingApp.controller('createUserController', function($rootScope, $scope, $http,  $window, $location) {

        console.log('In Create User controller')
        var controller = new CreateUserController($rootScope, $scope, $http,  $window, $location)

    });

    //################################### REPORTS ####################################

    accountingApp.controller('incomeStatementController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        console.log('income statement controller')
        var controller = new IncomeStatementController($rootScope, $scope, $http,  $window, $location)
    });


    accountingApp.controller('ownerEquityController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        console.log('owner equity controller')
        var controller = new OwnerEquityController($rootScope, $scope, $http,  $window, $location)
    });

    accountingApp.controller('balanceSheetController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        console.log('balance sheet controller')
        var controller = new BalanceSheetController($rootScope, $scope, $http,  $window, $location)
    });

    accountingApp.controller('financialRatioController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        console.log('Fin Ratio controller')
        var controller = new FinancialRatioController($rootScope, $scope, $http,  $window, $location)
    });

    accountingApp.controller('trialBalanceController', function($rootScope, $scope, $http,  $window, $location) {
        // create a message to display in our view
        console.log('Trial Balance controller')
        var controller = new TrialBalanceController($rootScope, $scope, $http,  $window, $location)
    });

    accountingApp.factory('httpRequestInterceptor', function ($q,  $window, $location) {
        return {
            request: function (config) {
                config.headers['Authorization'] = $window.sessionStorage.token;
                return config;
            },

            response: function(response){
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

