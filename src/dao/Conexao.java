package dao;

import java.sql.*;

public class Conexao {
	public Connection getConexao() {
        //System.out.println("Conectando ao Banco de Dados...");
        try {
            String url = "jdbc:mysql://localhost/contagemcred";
            Class.forName("com.mysql.jdbc.Driver"); 
            return DriverManager.getConnection(url, "root", "");
            //login e senha para o servidor: usuário (scc) senha(contagemcred)
            //url para o banco de dados: jdbc:mysql://localhost:3306/contagemcred
            //Usuário da máquina que está sendo usada como servidor e senha: usuário(root) senha(root)
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
