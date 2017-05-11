package acao;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ProjetoDAO;

public class CriaGrade implements Acao {
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws Exception{
		String curso = req.getParameter("curso");
		int matriz = Integer.parseInt(req.getParameter("matriz"));
		String cod_disciplina = req.getParameter("disciplina");
		int opcao = Integer.parseInt(req.getParameter("status"));
		String status = "";
		
		curso = determinaCurso(curso);
		switch (opcao){
		case 1:
			status = "Obrigatória";
		case 2:
			status = "Opção Limitada";
		}
		
		ProjetoDAO dao = new ProjetoDAO();
		int cod_ppc = dao.buscaProjeto(matriz, curso);
		boolean existente = dao.verificaGrade(cod_ppc, cod_disciplina, status);
		if(existente){
			req.setAttribute("msg", "Disciplina "+cod_disciplina+" já existe no Projeto de "+matriz+" do "+curso+"!");
			RequestDispatcher rd = req.getRequestDispatcher("/info.jsp");
			rd.forward(req, resp);
		} else{
			dao.criarGrade(cod_ppc, cod_disciplina, status);
			
			req.setAttribute("msg", "Disciplina "+cod_disciplina+" adicionada ao Projeto "+curso+" com sucesso!");
			RequestDispatcher rd = req.getRequestDispatcher("/info.jsp");
			rd.forward(req, resp);
		}
	}
	
	//Método para verificar qual curso será usado para a contagem de créditos
	public String determinaCurso(String sigla){
		String curso = "";
		switch (sigla){
		case "BCC":
			curso = "Bacharelado em Ciência da Computação";
			break;
		case "BMAT":
			curso = "Bacharelado em Matemática";
			break;
		case "LMAT":
			curso = "Licenciatura em Matemática";
			break;
		case "BNC":
			curso = "Bacharelado em Neurociência";
			break;
		}
		return curso;
	}
}
