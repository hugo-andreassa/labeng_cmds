package model.dao;

import db.DB;
import model.dao.impl.FornecedorDaoJDBC;

public class DaoFactory {
	
	public static FornecedorDao createFornecedorDao() {
		return new FornecedorDaoJDBC(DB.getConnection());
	}
	
}
