package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.MotorDao;
import model.dao.DaoFactory;
import model.entities.Motor;

public class MotorService {

	private MotorDao dao = DaoFactory.createMotorDao();
	
	public List<Motor> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Motor obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Motor obj) {
		dao.deleteById(obj.getId());
	}
}
