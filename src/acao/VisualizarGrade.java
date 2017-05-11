package acao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DisciplinaDAO;
import dao.ProjetoDAO;
import model.Disciplina;
import model.Grade;
import model.PPC;

public class VisualizarGrade implements Acao {
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws Exception{
		String cod_ppc = req.getParameter("ppc_id");
		//System.out.println("ppc_id: "+cod_ppc);
		
		ProjetoDAO p_dao = new ProjetoDAO();
		PPC p = p_dao.retornaProjeto(Integer.parseInt(cod_ppc));
		String p_nome = p.getCurso();
		String p_matriz = Integer.toString(p.getMatriz());
		List<Grade> grade = p_dao.gradeOrdenada(Integer.parseInt(cod_ppc));
		//System.out.println("grade: "+grade.size());

		req.setAttribute("ppc_grade", grade);
		req.setAttribute("curso", p_nome);
		req.setAttribute("matriz", p_matriz);
		RequestDispatcher rd = req.getRequestDispatcher("/grade.jsp");
		rd.forward(req, resp);
	}
}
