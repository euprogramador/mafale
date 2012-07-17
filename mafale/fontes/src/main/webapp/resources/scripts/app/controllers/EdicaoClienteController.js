function EdicaoClienteController($scope,Clientes,PortesCliente,TiposCliente, $routeParams,$location){
	$scope.cliente = {};
	$scope.portes = [];
	$scope.tipos = [];

	if ($routeParams.id!='novo'){
		showModal();
		Clientes.get({id:$routeParams.id},function(data){
			$scope.cliente = data;
			
			TiposCliente.get({},function(data){
				$scope.tipos = data.listagem;
				$scope.cliente.tipo = configureValue('id',$scope.cliente.tipo,data.listagem);
			},errorHandler);
			
			
			PortesCliente.get({},function(data){
				$scope.portes = data.listagem;
				$scope.cliente.porte = configureValue('id',$scope.cliente.porte,data.listagem);
			},errorHandler);
			
			hideModal();
		},errorHandler);
	} else {
		showModal();
		TiposCliente.get({},function(data){
			$scope.tipos = data.listagem;
			hideModal();
		},errorHandler);
		
		PortesCliente.get({},function(data){
			$scope.portes = data.listagem;
			hideModal();
		},errorHandler);
		
	}
		
	$scope.salvar = function(){
		showModal();
		
		
		
		Clientes.save({entidade:$scope.cliente},function(){
			hideModal();
			alert("Cliente salvo com sucesso");
			$location.path('/listagemCliente/1');
		},errorHandler);
	}; 
}

function configureValue(field,target,list){
	if (!target)
		return null;
	for (t in list){
		if (list[t][field]==target[field])
			return list[t];
	}
	return null;
}