<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cadastro de Disciplinas</title>
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
    		<h3>Cadastrar Disciplina</h3>
			<div class="form-group">
				<label for="nome">Nome:</label>
				<input type="text" name="nome" class="form-control" id="nome"/>
			</div>
			<div class="form-group">
				<label for="codigo">Código:</label>
				<input type="text" name="codigo" class="form-control" id="codigo"/>
			</div>
			<div class="form-group">
				<label for="t">Teoria:</label>
				<input type="text" name="t" class="form-control" id="t"/>
			</div>
			<div class="form-group">
				<label for="p">Prática:</label>
				<input type="text" name="p" class="form-control" id="p"/>
			</div>
			<div class="form-group">
				<label for="i">Individual:</label>
				<input type="text" name="i" class="form-control" id="i"/>
			</div>
			<input type="submit" class="btn btn-success" value="Cadastrar Disciplina">
			<input type="hidden" name="opcao" value="InsereDisciplina">
		</form>
    </div>
  </div>
</div>
</body>
</html>