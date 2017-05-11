package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Convalidacao;
import model.Disciplina;

public class ConvalidacaoDAO {
	private Connection connection;
	public ConvalidacaoDAO(){
		this.connection = new Conexao().getConexao();
	}
	
	public void fechaConexao() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void criaConvalidacao(Convalidacao c){
		String sql = "insert into convalidacoes (convalidation_id,cod_disciplina,cod_convalidacao,cod_ppc) "
				+ "values (?,?,?,?)";
        try {
            System.out.println("Iniciando Registro no Banco de Dados...");
            PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);

            //falta o return para preencher o objeto 'u'
            Convalidacao u = ultimaConvalidacao();
            //System.out.println("id: "+u.getId());
            if(c.getCod_disciplina() == u.getCod_disciplina() && c.getCod_ppc() == u.getCod_ppc()){
            	c.setId(u.getId());
            } else{
            	c.setId(u.getId()+1);
            }
            stmt.setInt(1, c.getId());
            stmt.setString(2, c.getCod_disciplina());
            stmt.setString(3, c.getCod_convalidacao());
            stmt.setInt(4, c.getCod_ppc());

            stmt.execute();
            stmt.close();
            //System.out.println("Registro Realizado com Sucesso!");
            //System.out.println();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}
	
	public Convalidacao ultimaConvalidacao(){
		Convalidacao c = new Convalidacao();
		String sql = "select * from convalidacoes where convalidation_id = (select max(convalidation_id) from "
				+ "convalidacoes)";
		System.out.println("Buscando convalidação...");
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				c.setCod_disciplina(rs.getString("cod_disciplina"));
				c.setCod_ppc(rs.getInt("cod_ppc"));
				c.setCod_convalidacao(rs.getString("cod_convalidacao"));
				c.setId(rs.getInt("convalidation_id"));
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return c;
	}
	
	//Busca se existe uma convalidação pelo código da disciplina e qual o ppc dela
	public Convalidacao buscaConvalidacao(String cod_disciplina, int cod_ppc){
		Convalidacao c = new Convalidacao();
		String sql = "select * from convalidacoes where cod_disciplina = ? and cod_ppc = ?";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, cod_disciplina);
			stmt.setInt(2, cod_ppc);
				
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				c.setCod_disciplina(rs.getString("cod_disciplina"));
				c.setCod_ppc(rs.getInt("cod_ppc"));
				c.setCod_convalidacao(rs.getString("cod_convalidacao"));
				c.setId(rs.getInt("convalidation_id"));
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return c;
	}
	
	public List<Convalidacao> listarConvalidacoes(){
		List<Convalidacao> convalidacoes = new ArrayList<>();
		String sql = "select c.cod_disciplina, c.cod_convalidacao, c.cod_ppc from convalidacoes c, disciplinas d "
				+ "where c.cod_disciplina = d.cod order by d.nome ";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Convalidacao c = new Convalidacao();
				c.setCod_disciplina(rs.getString("cod_disciplina"));
				c.setCod_convalidacao(rs.getString("cod_convalidacao"));
				c.setCod_ppc(Integer.parseInt(rs.getString("cod_ppc")));
				convalidacoes.add(c);
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return convalidacoes;
	}
	
	public String nomeDisciplina(String cod){
		String nome = "";
		String sql = "select nome from disciplinas where cod = ?";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, cod);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				nome = rs.getString("nome").toUpperCase();
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return nome;
	}
	
	public String nomeCurso(int ppc_id){
		String curso = "";
		String sql = "select curso from ppc where cod_ppc = ?";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, ppc_id);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				curso = rs.getString("curso").toUpperCase();
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return curso;
	}
	
	public int anoMatriz(int ppc_id){
		int matriz = 0;
		String sql = "select matriz from ppc where cod_ppc = ?";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, ppc_id);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				matriz = rs.getInt("matriz");
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return matriz;
	}
}
