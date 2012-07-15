angular.module('mafaleServices', ['ngResource']).
factory('TiposCliente', function($resource){
	return $resource('data/tiposcliente/:id', {}, {
 		query: {url:'data/tiposcliente?inicio=:inicio&numRegistros=:numRegistros',method:'GET', params:{inicio:0,numRegistros:10}}
	});
});


function montarPaginador(aoPaginarHandler){
	var paginacao = {};
	paginacao.numRegistros = 10;

	paginacao.atualizar = function(){
		paginacao.paginar(paginacao.paginaAtual);
	};

	paginacao.aoPaginar = aoPaginarHandler;
	
	paginacao.paginar = function(pagina){
		var inicio = (paginacao.numRegistros * pagina) - paginacao.numRegistros;
		paginacao.aoPaginar(inicio,paginacao.numRegistros);
	};
	
	paginacao.montarPaginacao = function(resultado){
		var numPaginas = parseInt(resultado.contagem/resultado.numRegistros);
		numPaginas = resultado.contagem % resultado.numRegistros == 0 ? numPaginas : numPaginas+1;
		
		paginacao.paginas = [];
		for (var i = 1; i<=numPaginas;i++){
			
			var numRegistros = resultado.numRegistros;
			var inicio = (numRegistros * i) - numRegistros;
		
	
			if (inicio == resultado.inicio)
				paginacao.paginaAtual = i;
			
			paginacao.paginas.push({numero:i,atual:(inicio == resultado.inicio ? 'current':'')});
		}
		paginacao.primeiro = 1;
		paginacao.anterior = paginacao.paginaAtual == 1 ? 1 : paginacao.paginaAtual - 1;
		paginacao.proximo = paginacao.paginaAtual == numPaginas ? numPaginas : paginacao.paginaAtual + 1;
		paginacao.ultimo = numPaginas;
		paginacao.totalRegistros = resultado.contagem;
		paginacao.contagemRegistros = resultado.listagem.length;
		
	};
	
	paginacao.$primeiro = function(){
		paginacao.paginar(1);
	};
	
	paginacao.$anterior = function(){
		var anterior = paginacao.paginaAtual-1;
		if (anterior < 1)
			anterior = 1;
		paginacao.paginar(anterior);
	};
	
	paginacao.$proximo = function(){
		var proximaPagina = paginacao.paginaAtual+1;
		if (proximaPagina > paginacao.ultimp)
			proximaPagina = paginacao.ultimo;
		paginacao.paginar(proximaPagina);
	};
	
	paginacao.$ultimo = function(){
		paginacao.paginar(paginacao.ultima);
	};
	
	return paginacao;
}
