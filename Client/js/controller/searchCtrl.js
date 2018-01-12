/*Controller für die Übersicht der Sprechstunden eines Studenten (Route: #/Student/Termine)*/
app.controller("searchCtrl", function ($scope, $window, $http, baseUrl)
{	
	$scope.result = {"data":[{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/41DPJmX7L4L.jpg","types":["NEW","USED"],"product_id":4242003607916,"name":"Siemens extreme Silence Power VSQ5X1230 Bodenstaubsauger Q5.0 (850 W, EEK B, 4 L Staubbeutelvolumen, Hochleistungs-Hygienefilter) schwarz","rating":4.3,"price_min":71.0,"price_max":266.95},{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/41cePGk0jEL.jpg","types":["NEW"],"product_id":4242003684030,"name":"Siemens VS06B1110 Bodenstaubsauger synchropower mit Beutel (700 W, EEK B, highPower Motor, powerSecure System) moonlight blue","rating":4.2,"price_min":74.99,"price_max":115.42},{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/41HUlk6fMML.jpg","types":["NEW","USED"],"product_id":8710103796299,"name":"Philips beutelloser Staubsauger PowerPro Compact (AAA-Energie-Label, 650 W, 1,5 L Staubvolumen, integrierte Bürste, weiß) FC9332\/09","rating":4.4,"price_min":99.99,"price_max":149.17},{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/414n5r7ZhhL.jpg","types":["NEW"],"product_id":5025155019948,"name":"Dyson DC33c Origin beutelloser Staubsauger (inkl. umschaltbarer Bodendüse mit Saugkraft-Regulierung, Kombi- und Polsterdüse, Bodenstaubsauger mit Energieeffizienzklasse A)","rating":4.1,"price_min":289.0,"price_max":498.87},{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/51Q1vKEUp6L.jpg","types":["NEW","USED"],"product_id":3221610134504,"name":"Rowenta RO3731EA Compact Power Cyclonic Bodenstaubsauger (750 W, EEK A, beutellos, 1,5 L, hocheffizienter Filter) schwarz\/blau","rating":4.2,"price_min":79.89,"price_max":500.31},{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/51FqyTAtlZL.jpg","types":["NEW","USED"],"product_id":4006508179374,"name":"Swirl M 40 AirSpace Staubsaugerbeutel für Miele Staubsauger, saugstark, verschließbare Halteplatte, 4 Beutel + 1 Filter","rating":4.7,"price_min":3.95,"price_max":78.55},{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/414TB9xYwpL.jpg","types":["NEW"],"product_id":7332543550425,"name":"AEG VAMPYR CE VX3-1-WR-A Staubsauger mit Beutel EEK A (700 Watt, inkl. Spezialdüsen zur Tierhaarentfernung und Hartbodendüse, 10 m Aktionsradius, 3,5 Liter Staubbeutelvolumen) rot","rating":4.0,"price_min":99.95,"price_max":121.99},{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/41lpcPzl4kL.jpg","types":["NEW"],"product_id":5060184206532,"name":"Duronic VC7 \/R HEPA Filter \u2013 Beutelloser, Aufrechter Standstaubsauger \u2013 inklusive eines extra HEPA-Filters und einer Turbobürste - verwandelbar in wenigen Sekunden von Stand- zu Handstaubsauger","rating":3.7,"price_min":36.99,"price_max":36.99},{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/31fgJAi7ReL.jpg","types":["NEW","USED"],"product_id":4012467939263,"name":"Dirt Devil DD698-1 Cavalier Kabelloser multifunktionaler Akku-Handstaubsauger ohne Beutel (21,6 V Li-Ion, Turbobürste, 3-in-1 Kombidüse) silber\/rot","rating":3.6,"price_min":107.99,"price_max":224.83},{"img":"https:\/\/images-eu.ssl-images-amazon.com\/images\/I\/41WoD4etZCL.jpg","types":["NEW","USED"],"product_id":4012467222037,"name":"Dirt Devil DD 2220-3 rebel 22HF Singlecyclone Staubsauger ohne Beutel  EEK B (800 Watt, 1,8 L Behältervolumen, HEPA Media 13 Ausblasfilter, inkl. Parkettbürste) metallgrau","rating":3.7,"price_min":19.0,"price_max":90.99}],"elements":10}
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
			.success(function (data)
			{	
				$scope.result = data;
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
});
