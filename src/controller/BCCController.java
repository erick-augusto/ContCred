package controller;

import java.util.List;

import javax.servlet.http.HttpServlet;

import model.Convalidacao;
import model.Disciplina;
import model.Grade;

public class BCCController extends HttpServlet {
	
	private String nome = "";
	private int ra = 0;
	private String sigla = "";
	private int matriz = 0;
	private String curso = "";
	private int cursado_bct = 0;
	private int cred_obrigatorio_bct = 0;
	private float p_bct = 0.0f;
	private int cursado_obrigatorio_curso = 0;
	private int cred_obrigatorio_curso = 0;
	private float p_curso = 0.0f;
	private int cursado_limitado = 0;
	private int cred_limitado_curso = 0;
	private float p_limitado = 0.0f;
	private int cursado_livre = 0;
	private int cred_curso_livre = 0;
	private float p_livre = 0.0f;
	private int cred_total_curso = 0;
	private int cursado_total = 0;
	private float p_total = 0.0f;
	private List<Disciplina> obrigatoria_bct;
	private List<Disciplina> obrigatoria_curso;
	private List<Disciplina> limitadas;
	private List<Disciplina> livres;
	private List<Disciplina> obrigatorias;
	private List<Grade> grade_ppc;
	private List<Grade> grade_bct;
	private List<Disciplina> cursadas;
	private List<Disciplina> nao_encontradas;
	private List<Disciplina> nao_catalogada;
	private List<String> pendentes;
	private List<Convalidacao> convalidacoes;
	private String path;
	private String fileName;
	
	public void montarRelatorio(){
		
	}
}
