function ListagemPorteClienteController($scope, PortesCliente, $routeParams){
	
	$scope.paginacao = montarPaginador(function(inicio,numRegistros){
		showModal();
		PortesCliente.query({'inicio':inicio,'numRegistros':numRegistros},function(data){
			$scope.resultado = data;
			$scope.paginacao.montarPaginacao(data);
			hideModal();
		},errorHandler);		
	});
	$routeParams.pagina || 1;
	$scope.paginacao.paginar($routeParams.pagina);

	$scope.remover = function(tipo){
		confirm('Confirma remoção?',function(){
			showModal();
			PortesCliente.remove({id:tipo.id},function(){
				alert('Porte de cliente removido com sucesso');
				hideModal();
				$scope.paginacao.atualizar();
			},errorHandler);
		});
	};
	
} 
