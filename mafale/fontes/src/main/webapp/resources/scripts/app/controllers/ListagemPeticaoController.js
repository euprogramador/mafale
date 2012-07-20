function ListagemPeticaoController($scope, Peticoes, $routeParams){
	
	$scope.paginacao = montarPaginador(function(inicio,numRegistros){
		showModal();
		
		var filtros = [{alvo:'servico.id|eq|long',valor:$routeParams.servicoId}];
		
		Peticoes.query({filtro:montarFiltro(filtros)},function(data){
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
			TiposServico.remove({id:tipo.id},function(){
				alert('Petição do serviço removida com sucesso');
				hideModal();
				$scope.paginacao.atualizar();
			},errorHandler);
		});
	};
	
} 
