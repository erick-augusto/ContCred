<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cadastro de Convalidações</title>
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
    	<form role="form" action="controller" method="post">
    		<h3>Cadastrar Convalidação</h3>
			<div class="form-group">
				<label for="cod_disciplina">Código da Disciplina que será convalidada:</label>
				<input type="text" name="cod_disciplina" class="form-control" id="cod_disciplina"/>
			</div>
			<div class="form-group">
				<label for="cod_convalidacao">Código da Disciplina no Projeto:</label>
				<input type="text" name="cod_convalidacao" class="form-control" id="cod_convalidacao"/>
			</div>
			<div class="form-group">
				<label for="curso">Curso:</label>
				<select class="form-control" name="curso" id="curso">
					<option value="BCC">Bacharelado em Ciência da Computação</option>
					<option value="BCT">Bacharelado em Ciência e Tecnologia</option>
					<option value="BMAT">Bacharelado em Matemática</option>
					<option value="BNC">Bacharelado em Neurociência</option>
					<option value="LMAT">Licenciatura em Matemática</option>	
				</select>
			</div>
			<div class="form-group">
				<label for="matriz">Matriz do Projeto:</label>
				<input type="text" name="matriz" class="form-control" id="matriz"/>
			</div>
			<input type="submit" class="btn btn-success" value="Cadastrar Convalidação">
			<input type="hidden" name="opcao" value="NovaConvalidacao">
		</form>
    </div>
  </div>
</div>
</body>
</html>
