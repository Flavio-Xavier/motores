package model.dao;

import db.DB;
import model.dao.impl.EquipamentoDaoJDBC;
import model.dao.impl.MotorDaoJDBC;
import model.dao.impl.AreaDaoJDBC;

public class DaoFactory {

	public static AreaDao createAreaDao() {
		return new AreaDaoJDBC(DB.getConnection());
	}
	
	public static EquipamentoDao createEquipamentoDao() {
		return new EquipamentoDaoJDBC(DB.getConnection());
	}
	
	public static MotorDao createMotorDao() {
		return new MotorDaoJDBC(DB.getConnection());
	}
}
