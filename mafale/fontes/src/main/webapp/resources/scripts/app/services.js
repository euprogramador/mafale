
angular.module('mafaleServices', ['ngResource']).
factory('AssuntosPeticao', function($resource){
	return $resource('data/assuntospeticao/:id', {}, {
 		query: {url:'data/assuntospeticao?inicio=:inicio&numRegistros=:numRegistros',method:'GET', params:{inicio:0,numRegistros:10}}
	});
}).
factory('TiposServico', function($resource){
	return $resource('data/tiposservico/:id', {}, {
 		query: {url:'data/tiposservico?inicio=:inicio&numRegistros=:numRegistros',method:'GET', params:{inicio:0,numRegistros:10}}
	});
}).
factory('TiposCliente', function($resource){
	return $resource('data/tiposcliente/:id', {}, {
 		query: {url:'data/tiposcliente?inicio=:inicio&numRegistros=:numRegistros',method:'GET', params:{inicio:0,numRegistros:10}}
	});
}).
factory('PortesCliente', function($resource){
	return $resource('data/portescliente/:id', {}, {
 		query: {url:'data/portescliente?inicio=:inicio&numRegistros=:numRegistros',method:'GET', params:{inicio:0,numRegistros:10}}
	});
}).
factory('Clientes', function($resource){
	return $resource('data/clientes/:id', {}, {
 		query: {url:'data/clientes?inicio=:inicio&numRegistros=:numRegistros',method:'GET', params:{inicio:0,numRegistros:10}}
	});
});





function alert(msg){
	$( "<div title='Sistema'><p>"+msg+"</p></div>" ).dialog({
		modal: true,
		buttons: {
			Ok: function() {
				$( this ).dialog( "close" );
			}
		}
	});
}

function confirm(msg,handler){
	$( "<div title='Sistema'><p>"+msg+"</p></div>" ).dialog({
		modal: true,
		buttons: {
			Sim: function() {
				handler();
				$( this ).dialog( "close" );
			},
			'Não': function(){
				$( this ).dialog( "close" );
			}
		}
	});
}
emptyFn = function(){};
errorHandler = function(error){
	if (error.status == 400 && error.data.errors){
		for(var i=0; i<error.data.errors.length;i++){
			var el = $("#"+error.data.errors[i].category);
			if (el.length==0){
				alert("campo para mensagem não encontrado "+error.data.errors[i].category);
				continue;
			}
			el.removeClass("hidden");
			el.html(error.data.errors[i].message);
			$("#"+el.attr('for'))
			.addClass("inputError")
			.keyup(function(element){
				el.addClass('hidden');
				$(this).removeClass("inputError");
			});
		}
	}else {
		var msg = error.headers("mensagem");
		if (msg){
			alert(msg);
		} else {
			alert('Ocorreu um erro de comunicação com o servidor execute a operação novamente');
		}
	}
	hideModal();
};

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


