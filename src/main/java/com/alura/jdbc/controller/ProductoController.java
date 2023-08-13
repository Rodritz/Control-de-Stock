package com.alura.jdbc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;
import com.mysql.cj.result.IntegerValueFactory;

public class ProductoController {
	
	private ProductoDAO productoDao;
	
	//con el sig constructor, el productoDao al ser instanciado ya genera una conexion
	public ProductoController() {	
		/*var factory = new ConnectionFactory();
        this.productoDao = new ProductoDAO(factory.recuperaConexion());*/
		this.productoDao = new ProductoDAO(new ConnectionFactory().recuperaConexion());
	}

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id){	
		return productoDao.modificar(nombre, descripcion, cantidad, id);
	}

	public int eliminar(Integer id) throws SQLException {
		return productoDao.eliminar(id);		
	}

	public List<Producto> listar() {
		return  productoDao.listar();
	}
	
	public List<Producto> listar(Categoria categoria){
		return productoDao.listar(categoria.getId());		
	}

    public void guardar(Producto producto, Integer categoriaId) {
    	producto.setCategoriaId(categoriaId);
    	productoDao.guardar(producto);	
	}	
}
		
