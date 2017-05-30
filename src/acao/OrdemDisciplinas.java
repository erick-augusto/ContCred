package acao;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DisciplinaDAO;
import model.Disciplina;

public class OrdemDisciplinas implements Acao {
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws Exception{
		String letra = req.getParameter("letra");
		DisciplinaDAO dao = new DisciplinaDAO();
		List<Disciplina> letra_disciplinas = dao.letraDisciplina(letra);
		req.setAttribute("inicial", letra);
		req.setAttribute("por_letra", letra_disciplinas);
		RequestDispatcher rd = req.getRequestDispatcher("/disciplinasAlfabetico.jsp");
		rd.forward(req, resp);
	}
}
