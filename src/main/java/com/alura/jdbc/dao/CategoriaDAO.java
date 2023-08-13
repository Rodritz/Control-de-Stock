package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

public class CategoriaDAO {
	/*PATRON DAO QUE CENTRALIZA LAS OPERACIONES DE ACCESO A UN RECURSO ESPECIFICO*/

	private Connection con;

	public CategoriaDAO(Connection con) {
		this.con = con;
	}

	public List<Categoria> listar() {
		List<Categoria> resultado = new ArrayList<>();

		try {
			final PreparedStatement statement = con.prepareStatement("SELECT ID, NOMBRE FROM CATEGORIA");
			try (statement) {
				final ResultSet resultSet = statement.executeQuery();

				try (resultSet) {
					while (resultSet.next()) {
						Categoria categoria = new Categoria(resultSet.getInt("ID"), resultSet.getString("NOMBRE"));
						resultado.add(categoria);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resultado;
	}

	public List<Categoria> listarConProductos() {
		List<Categoria> resultado = new ArrayList<>();

		try {
			var querySelect = "SELECT C.ID, C.NOMBRE, P.ID, P.NOMBRE, P.CANTIDAD "
					+ "FROM CATEGORIA C "
					+ "INNER JOIN PRODUCTO P ON C.ID = P.CATEGORIA_ID ";
			System.out.println(querySelect);
			
			final PreparedStatement statement = con.prepareStatement(querySelect);
			
			try (statement) {
				final ResultSet resultSet = statement.executeQuery();

				try (resultSet) {
					while (resultSet.next()) {
						Integer categoriaId = resultSet.getInt("ID");
						String categoriaNombre = resultSet.getString("NOMBRE");
						
						Categoria categoria = resultado
								.stream()
								.filter(cat -> cat.getId().equals(categoriaId))
								.findAny().orElseGet(()-> {
									Categoria cat = new Categoria(
											categoriaId, 
											categoriaNombre);
									
									resultado.add(cat);
									
									return cat;
								});	
						
						Producto producto = new Producto(
								resultSet.getInt("P.ID"),
								resultSet.getString("P.NOMBRE"),
								resultSet.getInt("P.CANTIDAD"));
						
						categoria.agregar(producto);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resultado;
	}

}
/*-------IMPORTANTE 1------*/

/*Para evitar el fallo por SQL Injection debemos utilizar la interfaz PreparedStatement
el PreparedStatement trata los parámetros del comando SQL para que caracteres y
comandos especiales sean tratados como strings.*/

/*-------IMPORTANTE 2 => TRY WITH RESOURCES (utiliza recursos autoCloseable)------*/

/*desde la versión 9 de Java es posible utilizar variables final dentro 
de la declaración de los recursos e implementamos la interfaz autoCloseable*/

/*--desde la versión 7 de Java hay un recurso llamado Try With Resources. 
El Try With Resources nos permite declarar recursos que van a ser utilizados 
en un bloque de try catch con la certeza de que estos recursos van a ser cerrados 
o finalizados automáticamente después de la ejecución del bloque. Un requisito 
para eso es que estos recursos deben implementar la interfaz autoCloseable.--

try(ResultSet resultSet = statement.getGeneratedKeys();){
	while(resultSet.next()) {
		System.out.println(
				String.format(
						"Fue insertado el producto de ID %d", 
						resultSet.getInt(1)));
	}
}*/

/*---antes de java 7 debiamos cerrar las conexiones por medio de comandos--

ResultSet resultSet = statement.getGeneratedKeys();

while(resultSet.next()) {
	System.out.println(
			String.format(
					"Fue insertado el producto de ID %d", 
					resultSet.getInt(1)));
}

resultSet.close();*/
