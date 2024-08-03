package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.AreaDao;
import model.dao.DaoFactory;
import model.entities.Area;

public class AreaService {

	private AreaDao dao = DaoFactory.createAreaDao();
	
	public List<Area> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Area obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
}
