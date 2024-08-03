package model.dao;

import java.util.List;

import model.entities.Area;

public interface AreaDao {
	
	void insert(Area obj);
	void update(Area obj);
	void deleteById(Integer id);
	Area findById(Integer id);
	List<Area> findAll();

}
