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
	"/usr/watchedProducts" : 
	{
		controller: 'watchedProductCtrl',
		templateUrl: 'pages/watched.html',
		requireLogin: false
	}
};

/*Die Routen festlegen*/
app.config(function($routeProvider, $httpProvider)
{
	for(var path in window.routes)
	{
		$routeProvider.when(path, window.routes[path]);
	}
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
			$rootScope.userIsLoggedIn = false;
		}
		if($rootScope.user != undefined)
		{
			$rootScope.userIsLoggedIn = true;
		}
		console.log($rootScope.userIsLoggedIn);
		onResize();
	});
	
	/*Wenn eine View gewechselt wird, wird überprüft ob der Benutzer für den Zugriff authorisiert ist, wenn nicht wird er auf die entsprechende Login-Seite weitergeleitet*/
	$rootScope.$on("$locationChangeStart", function (event, next, current)
	{
		for(var i in window.routes)
		{
			if(next.indexOf(i) != -1)
			{
				if(window.routes[i].requireUserLogin && !$rootScope.userIsLoggedIn)
				{
					$window.location.href = baseUrl + "/usr";	
					console.log("User ist nicht angemeldet");
					event.preventDefault();					
				}				
			}
		}
	})
	
});