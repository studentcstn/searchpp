/*Controller für das Anmelden eines Dozenten (Route: #/Dozent/Login)*/
app.controller("dozentLoginCtrl", function($scope, $rootScope, $window, $sessionStorage)
{
	/*Dozent anmelden*/
	$scope.loginError;

	$scope.login = function()
	{
		$scope.loginError = undefined;

		/*Prüfung ob Eingaben valide sind*/
		if(angular.isDefined($scope.dozent) 
			 && angular.isDefined($scope.dozent.email) 
			 && angular.isDefined($scope.dozent.pwd))
		{	
			/*Daten an AuthenticationService senden*/
					if (result.status == 'ok') 
					{
						/*Benutzer in $rootScope speichern*/
						$rootScope.user = $sessionStorage.currentUser;
			
						/*Benutzeränderung mitteilen*/
						$rootScope.$broadcast("userChange", $rootScope.user);	//Broadcast changes
						
						/*Auf Terminübsericht weiterleiten*/
						$window.location.href = '#/Dozent/Termine';
					} 
					else 
					{
						/*Fehler anzeigen*/
						$scope.loginError = result.code;
					}	
		}
	}
	
});