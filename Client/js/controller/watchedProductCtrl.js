/*Controller für die Übersicht der Sprechstunden eines Studenten (Route: #/Student/Termine)*/
app.controller("watchedProductCtrl", function ($rootScope, $scope, $window, $http, baseUrl)
{
    $scope.logout = function()
    {
        $rootScope.user = undefined;
        $rootScope.$broadcast("userChange", $rootScope.user);
        $window.location.href = "#/";
    };

	$scope.showDetails = function(item)
	{
		$rootScope.productDetail = item;
		$window.location.href = '#/product';
	}
	
	$scope.getHistory = function(item)
	{
		$rootScope.productHistory = item;
		$window.location.href = '#/usr/watchedProducts/history';
	}

	$scope.changeDate = function(item)
	{
		$rootScope.productSetDate = item;
		$window.location.href = '#/usr/watchedProducts/changeDate';
	}


	
	$scope.removeList = function(item)
	{		
		$http.delete(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts")
		.success(function ()
		{	
			console.log("Result!!");				
			$scope.getData();
		})
		.error(function (error, status)
		{
			console.log(error);
			console.log(status);
			console.log("Error!!");
		});

	}
	
	$scope.remove = function(item)
	{
		console.log(item);
		$http.delete(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts/" + item.product_id)
		.success(function ()
		{	
			console.log("Result!!");	
		})
		.error(function (error, status)
		{
			console.log(error);
			console.log(status);
			console.log("Error!!");
		});

		$scope.getData();
	}
	
	$scope.getData = function()
	{
		if($rootScope.user !== undefined)
		{
			$http.get(baseUrl + "/usr/" + $rootScope.user + "/watchedProducts")
			.success(function (data)
			{	
				$scope.result = data;
				console.log("Result!!");
				console.log(data);			
			})
			.error(function (error, status)
			{
				console.log(error);
				console.log(status);
				console.log("Error!!");
			});
		}
	}
});
