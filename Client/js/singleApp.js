/*Initialisierung des Angular Moduls*/
'use strict';
var app = angular.module("app",["ngRoute", "ngStorage", "ngSanitize"]);

/*URL des Servers*/
app.constant("baseUrl", "http://localhost:8080/myapp");

/*Routen zum navigieren, requireDozLogin/requireStudLogin/requireAdminLogin werden verwendet, 
um Bereiche vor unauthorisierten Zugriff zu schützen*/
window.routes = 
{
	"/" : 
	{
		controller: 'searchCtrl',
		templateUrl: 'pages/search.html',
		requireLogin: false
	},
	"/product" : 
	{
		controller: 'productDetailCtrl',
		templateUrl: 'pages/detail.html',
		requireLogin: false
	},
	"/Dozent/Login" : 
	{
		controller: 'dozentLoginCtrl',
		templateUrl: 'pages/dozLogin.html',
		requireLogin: false,
	}
};

/*Wird verwendet um zu prüfen, ob der Token des Dozenten noch gültig ist*/
app.factory('httpInterceptorService', function ($q, $window, $rootScope, $injector)
{
    var service = 
	{
        request: request,
        responseError: responseError
    };

    return service;

    function request(config) 
	{
        // do some logic
        return config;
    }

    function responseError(rejection) 
	{
        if (rejection.status == 403) 
		{
            // they were unauthorised.
			$rootScope.user = undefined;
		
			$rootScope.$broadcast("userChange", $rootScope.user);
			$window.location.href = '#/Dozent/Login';

			$.alert({
			    title: 'Benutzer',
				type: 'red',
			    content: 'Benutzer-Session abgelaufen. Neuanmeldung nötig!',
			});
        }

        return $q.reject(rejection);
    }
});

/*Die Routen festlegen*/
app.config(function($routeProvider, $httpProvider, $sceProvider)
{
	$sceProvider.enabled(false);

	for(var path in window.routes)
	{
		$routeProvider.when(path, window.routes[path]);
	}

	$httpProvider.interceptors.push('httpInterceptorService');
});


/*Wird beim Öffnen der Website geladen*/
app.run(function($rootScope, $window, $http, $sessionStorage)
{
	/*Listener, der prüft ob sich ein Benutzer angemeldet oder abgemeledt hat*/
	$rootScope.$on("userChange", function(events, args)
	{		
		$rootScope.user = args;
		if($rootScope.user == undefined) //Kein Benutzer angemeldet
		{
			$rootScope.adminIsLoggedIn = false;
			$rootScope.dozIsLoggedIn = false;
			$rootScope.studIsLoggedIn = false;
		}
		if($rootScope.user != undefined)
		{
			if($rootScope.user.type == "dozent")
			{
				$rootScope.dozIsLoggedIn = true;
				if($rootScope.user.isAdmin == 1)
					$rootScope.adminIsLoggedIn = true;
				else
					$rootScope.adminIsLoggedIn = false;
			}
			else if($rootScope.user.type == "student")
				$rootScope.studIsLoggedIn = true;
		}

		onResize();
	});
	
	/*Prüft ob ein Benutzer im $sessionStorage gespeichert ist und meldet diesen dann an*/
	if ($sessionStorage.currentUser) 
	{
		$http.defaults.headers.common.Authorization = 'Bearer ' + $sessionStorage.currentUser.token;
				
		if($sessionStorage.currentUser.type == "dozent")
		{
			$rootScope.user = {type: "dozent", name: $sessionStorage.currentUser.name, id: $sessionStorage.currentUser.id, isAdmin: $sessionStorage.currentUser.isAdmin}; //set user
		}
		else
		{
			$rootScope.user = {type: "student", name: "", token: $sessionStorage.currentUser.token}; //set user
		}
		$rootScope.$broadcast("userChange", $rootScope.user);
	}
		
	/*Wenn eine View gewechselt wird, wird überprüft ob der Benutzer für den Zugriff authorisiert ist, wenn nicht wird er auf die entsprechende Login-Seite weitergeleitet*/
	$rootScope.$on("$locationChangeStart", function (event, next, current)
	{
		for(var i in window.routes)
		{
			if(next.indexOf(i) != -1)
			{
				if(window.routes[i].requireDozLogin && !$rootScope.dozIsLoggedIn)
				{
					$window.location.href = '#/Dozent/Login';
					console.log("Dozent ist nicht angemeldet");
					event.preventDefault();					
				}				
				if(window.routes[i].requireAdminLogin && !$rootScope.adminIsLoggedIn)
				{
					$window.location.href = '#/Dozent/Login';
					console.log("Admin ist nicht angemeldet");
					event.preventDefault();					
				}
				if(window.routes[i].requireStudLogin && !$rootScope.studIsLoggedIn)
				{
					 $window.location.href = '#/Student/Login';
					console.log("Student ist nicht angemeldet");
					event.preventDefault();
				}
			}
		}
	})
	
});