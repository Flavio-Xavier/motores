package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Area;

public class AreaService {

	public List<Area> findAll() {
		List<Area> list = new ArrayList<Area>();
		list.add(new Area(1, "SPA"));
		list.add(new Area(2, "Fusao"));
		list.add(new Area(3, "Macharia"));
		return list;
	}
}
