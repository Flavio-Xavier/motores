package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.AreaDao;
import model.dao.DaoFactory;
import model.entities.Area;

public class AreaService {

	private AreaDao dao = DaoFactory.createAreaDao();
	
	public List<Area> findAll() {
		// List<Area> list = new ArrayList<Area>();
		// list.add(new Area(1, "SPA"));
		// list.add(new Area(2, "Fusao"));
		// list.add(new Area(3, "Macharia"));
		// return list;
		return dao.findAll();
	}
}
