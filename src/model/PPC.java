package model;

public class PPC {
	private int cod_ppc;
	private String curso;
	private int matriz;
	private int cred_obrigatorias;
	private int cred_limitadas;
	private int cred_livres;
	
	public int getCod_ppc() {
		return cod_ppc;
	}
	public void setCod_ppc(int cod_ppc) {
		this.cod_ppc = cod_ppc;
	}
	public String getCurso() {
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public int getMatriz() {
		return matriz;
	}
	public void setMatriz(int matriz) {
		this.matriz = matriz;
	}
	public int getCred_obrigatorias() {
		return cred_obrigatorias;
	}
	public void setCred_obrigatorias(int cred_obrigatorias) {
		this.cred_obrigatorias = cred_obrigatorias;
	}
	public int getCred_limitadas() {
		return cred_limitadas;
	}
	public void setCred_limitadas(int cred_limitadas) {
		this.cred_limitadas = cred_limitadas;
	}
	public int getCred_livres() {
		return cred_livres;
	}
	public void setCred_livres(int cred_livres) {
		this.cred_livres = cred_livres;
	}
}
