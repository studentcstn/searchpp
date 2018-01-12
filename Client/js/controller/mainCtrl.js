/*Controller für den Body, wird nur für ngRoute benötigt (Route: #/)*/
app.controller("mainCtrl", function($scope, $rootScope, $window, $http, baseUrl)
{
	/*Sprechstundenanfrage versenden*/
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
			console.log(product);
			console.log(request);
			/*Anzeige, dass die Daten versendet werden anzeigen*/
			$scope.isSending = true;

			//POST /Student/<email>/Termine/
			console.log(baseUrl + "/products");
			console.log(request);
			$http.get(baseUrl + "/products" , {params : request})
			.success(function ()
			{	
				/*Beide Anzeigen zurücksetzten*/
				$scope.isSending = false;
				$scope.sendError = 200;	
				
				/*Bestätigungsdialog*/
				jQuery.confirm({
					title: 'Anfrage gesendet',
					content: 'Die Anfrage wurde gesendet und muss nun über die E-Mail akzeptiert werden!',
					buttons: {   
						ok: {
							text: "Ok",
							btnClass: 'btn-primary',
							keys: ['enter'],
							action: function(){
								/*Auf die Hauptseite weiterleiten*/
								$window.location.href = '#/';
							}
						}
					}
				});
				
			})
			.error(function (error, status)
			{
				/*Fehler auf Seite anzeigen*/
				$scope.isSending = false;
				$scope.sendError = status;
			});
		}
	}
});
