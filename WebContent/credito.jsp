<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Contagem de Créditos</title>
<script type="text/javascript">
	function abrirRelatorio(){
		document.getElementById('opcao').value = "abrir";
		//Passar o valor do ano pelo hidden para não ser perdido no método post
		var matriz = document.getElementById("matriz");
		var selected = matriz.options[matriz.selectedIndex].value;
		document.getElementById('ano').value = selected;
	}
	function baixarRelatorio(){
		document.getElementById('opcao').value = "baixar";
		//Passar o valor do ano pelo hidden para não ser perdido no método post
		var matriz = document.getElementById("matriz");
		var selected = matriz.options[matriz.selectedIndex].value;
		document.getElementById('ano').value = selected;
	}
	var matrizCurso = {};
	matrizCurso['BCC'] = ['2006', '2009', '2010','2015'];
	matrizCurso['BMAT'] = ['2010', '2012', '2017'];
	matrizCurso['BNC'] = ['2010', '2015'];
	matrizCurso['LMAT'] = ['2010'];

	function ChangeCurso() {
	    var cursos = document.getElementById("curso");
	    var listaMatrizes = document.getElementById("matriz");
	    var selected = cursos.options[cursos.selectedIndex].value;
	    while (listaMatrizes.options.length) {
	    	listaMatrizes.remove(0);
	    }
	    var matrizes = matrizCurso[selected];
	    if (matrizes) {
	        var i;
	        for (i = 0; i < matrizes.length; i++) {
	            var matriz = new Option(matrizes[i], i);
	            listaMatrizes.options.add(matriz);
	        }
	    }
	}
</script>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<style>
    .navbar {
      margin-bottom: 50px;
      border-radius: 0;
    }

     .jumbotron {
      background-color: #008000;
      color: #FFFFFF;
      margin-bottom: 0;
      height: 150px;
    }

    .row.content {
    	height: 500px;
    }

    .fundo{
    	background-color: #CCFFCC;
    }
    
    .menu{
    	background-color: #F2F2F2;
    }
    
    .sidenav {
      background-color: #FFFFFF;
      height: 100%;
    }

    footer {
      background-color: #CCFFCC;
      padding: 10px;
    }
</style>
</head>
<body>
<div class="jumbotron">
  <div class="container text-center">
    <h2>Sistema de Contagem de Créditos</h2>
  </div>
</div>

<div class="container-fluid">
  <div class="row content">
   <div class="col-sm-2 sidenav hidden-xs">
      <ul class="nav nav-pills nav-stacked menu row content">
        <li class="fundo"><a href="index.jsp">Home</a></li>
        <li><a href="disciplina.jsp">Disciplina</a></li>
        <li><a href="ppc.jsp">Projeto Pedagógico</a></li>
        <li><a href="convalidacao.jsp">Convalidação</a></li>
        <li><a href="credito.jsp">Contagem de Créditos</a></li>
      </ul><br>
    </div>
    <div class="col-sm-10">
    	<form role="form" action="upload" method="post" enctype="multipart/form-data">
	    	<h3>Inicar Contagem de Créditos</h3>
	    	<div class="form-group">
				<label for="nome">Nome do Aluno:</label>
				<input type="text" name="nome" class="form-control" id="nome"/>
			</div>
			<div class="form-group">
				<label for="ra">RA:</label>
				<input type="text" name="ra" class="form-control" id="ra"/>
			</div>
			<div class="form-group">
				<label for="curso">Curso:</label>
				<select class="form-control" name="curso" id="curso" onchange="ChangeCurso()">
					<option value="">Selecione o Curso</option>
					<option value="BCC">Bacharelado em Ciência da Computação</option>
					<option value="BMAT">Bacharelado em Matemática</option>
					<option value="LMAT">Licenciatura em Matemática</option>
					<option value="BNC">Bacharelado em Neurociência</option>
				</select>
			</div>
			<div class="form-group">
				<label for="matriz">Matriz:</label>
				<select class="form-control" name="matriz" id="matriz">
				</select>
			</div>
			<input type="hidden" name="ano" value="" id="ano"/>
			<div class="form-group">
				<label for="file">Arquivo:</label>
				<input type="file" name="file" class="form-control" id="file"/>
			</div>
			<input type="hidden" name="opcao" value="" id="opcao"/>
			<input type="submit" class="btn btn-success" value="Exibir Relatório" onClick="return abrirRelatorio()"/>
			<input type="submit" class="btn btn-success" value="Baixar Relatório" onClick="return baixarRelatorio()"/>
		</form>
    </div>
  </div>
</div>
</body>
</html>
