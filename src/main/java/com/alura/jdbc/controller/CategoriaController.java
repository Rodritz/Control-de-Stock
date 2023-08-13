package com.alura.jdbc.controller;

import java.util.ArrayList;
import java.util.List;

import com.alura.jdbc.dao.CategoriaDAO;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;

public class CategoriaController {
	
	private CategoriaDAO categoriaDAO;

	public CategoriaController() {
		//inicializamos la conectionFactory
		ConnectionFactory factory = new ConnectionFactory();
		//inicializamos la categoriaDao enviando una conexion del pool de conexiones
		this.categoriaDAO = new CategoriaDAO(factory.recuperaConexion());
	}

	public List<Categoria> listar() {
		return categoriaDAO.listar();
	}

    public List<Categoria> cargaReporte() {
        return this.categoriaDAO.listarConProductos();
    }

}
