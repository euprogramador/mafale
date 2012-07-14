angular.module('mafaleServices', ['ngResource']).
factory('TiposCliente', function($resource){
	return $resource('data/tiposcliente/:id', {}, {
 		list: {method:'GET', params:{}, isArray:true},
		update: {method:'PUT', params:{}},
		save: {method:'POST', params:{}},
		remove: {method:'DELETE', params:{}}
	});
});