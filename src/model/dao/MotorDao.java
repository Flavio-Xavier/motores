package model.dao;

import java.util.List;

import model.entities.Area;
import model.entities.Motor;

public interface MotorDao {

	void insert(Motor obj);
	void update(Motor obj);
	void deleteById(Integer id);
	Motor findById(Integer id);
	List<Motor> findAll();
	List<Motor> findByArea (Area area);
}
