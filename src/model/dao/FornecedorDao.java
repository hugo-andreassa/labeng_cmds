package model.dao;

import java.util.List;

import model.entities.Fornecedor;

public interface FornecedorDao {
	
	void insert(Fornecedor obj);
	void update(Fornecedor obj);
	void deleteById(Integer id);
	Fornecedor findById(Integer id);
	List<Fornecedor> findAll();
	
}
