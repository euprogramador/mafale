function EdicaoTipoClienteController($scope,TiposCliente, $routeParams){

	$scope.tipo = {};
	
	if ($routeParams.id!='novo'){
		showModal();
		TiposCliente.get({id:$routeParams.id},function(data){
			$scope.tipo = data;
			console.log(data);
			hideModal();
		},errorHandler);
	}
		
	$scope.salvar = function(){
		showModal();
		TiposCliente.save({tipoCliente:$scope.tipo},function(){
			hideModal();
			alert("Tipo salvo com sucesso");
		},errorHandler);
	};
}