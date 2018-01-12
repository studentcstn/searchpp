/*Controller für die Login-Anzeige rechts auf der Navigationsleiste*/
app.controller("userCtrl", function($scope, $rootScope)
{
	/*Benutzer abmelden*/
	$scope.logout = function()
	{
		$rootScope.user = undefined;
		
		/*Benutzeränderung mitteilen*/
		$rootScope.$broadcast("userChange", $rootScope.user);
	};
});
