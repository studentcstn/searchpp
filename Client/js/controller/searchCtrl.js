/*Controller für die Übersicht der Sprechstunden eines Studenten (Route: #/Student/Termine)*/
app.controller("searchCtrl", function ($rootScope, $scope, $window, $location, $http, baseUrl)
{	
	if($rootScope.productList !== undefined)
		$scope.result = $rootScope.productList;
	
	var params = $location.search();
	if("token" in params)
	{
		$rootScope.user = params.token;
		$rootScope.$broadcast("userChange", $rootScope.user);
	}
	
	$scope.login = function()
	{
		$window.location.href = baseUrl + "/usr";			
	}
	
	$scope.search = function(product)
	{
		/*Fehleranzeige in der View zurücksetzen*/
		if($scope.productSearch.$valid)
		{
			/*Kopie der Anfrage anlegen, dass nichts in dem Formular verändert wird*/
			var request = {
				search_text: product.name
			};
			
			if("min" in product)
				request.price_min = product.min*100;
			if("max" in product)
				request.price_max = product.max*100;
			if("used" in product)
				request.used = product.used;
			$rootScope.request = request;
			console.log(product);
			console.log(request);
			/*Anzeige, dass die Daten versendet werden anzeigen*/
			$scope.isSending = true;

			//POST /Student/<email>/Termine/
			console.log(baseUrl + "/products");
			console.log(request);
			$http.get(baseUrl + "/products" , {params : request})
			.success(function (data)
			{	
				$rootScope.productList = $scope.result = data;
				console.log("Result!!");
				console.log(data);
				console.log($scope.result);
				/*Bestätigungsdialog*/
				
			})
			.error(function (error, status)
			{
				console.log(error);
				console.log(status);
				console.log("Error!");
			});
		}
	}
	
	$scope.showDetails = function(item)
	{
		console.log(item);
			$rootScope.productDetail = item;
			$window.location.href = '#/product';
	}
});
