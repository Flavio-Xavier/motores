package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.EquipamentoDao;
import model.entities.Area;
import model.entities.Equipamento;

public class EquipamentoDaoJDBC implements EquipamentoDao {

    private Connection conn;
    
    public EquipamentoDaoJDBC(Connection conn) {
        this.conn = conn;
    }
    
    @Override
    public void insert(Equipamento obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "INSERT INTO Equipamento (Nome, id_area) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
            
            st.setString(1, obj.getName());
            st.setInt(2, obj.getArea().getId());
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Equipamento obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "UPDATE Equipamento SET Nome = ?, id_area = ? WHERE Id = ?");
            
            st.setString(1, obj.getName());
            st.setInt(2, obj.getArea().getId());
            st.setInt(3, obj.getId());
            
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM Equipamento WHERE Id = ?");
            
            st.setInt(1, id);
            
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Equipamento findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                "SELECT Equipamento.*, area.Nome as AreaName "
                + "FROM Equipamento INNER JOIN area ON Equipamento.id_area = area.Id "
                + "WHERE Equipamento.Id = ?");
            
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Area area = instantiateArea(rs);
                Equipamento obj = instantiateEquipamento(rs, area);
                return obj;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Equipamento> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                "SELECT Equipamento.*, area.Nome as AreaName "
                + "FROM Equipamento INNER JOIN area ON Equipamento.id_area = area.Id "
                + "ORDER BY Equipamento.Nome");
            
            rs = st.executeQuery();
            
            List<Equipamento> list = new ArrayList<>();
            Map<Integer, Area> map = new HashMap<>();
            
            while (rs.next()) {
                Area area = map.get(rs.getInt("id_area"));
                
                if (area == null) {
                    area = instantiateArea(rs);
                    map.put(rs.getInt("id_area"), area);
                }
                
                Equipamento obj = instantiateEquipamento(rs, area);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Equipamento> findByArea(Area area) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                "SELECT Equipamento.*, area.Nome as AreaName "
                + "FROM Equipamento INNER JOIN area ON Equipamento.id_area = area.Id "
                + "WHERE id_area = ? ORDER BY Equipamento.Nome");
            
            st.setInt(1, area.getId());
            
            rs = st.executeQuery();
            
            List<Equipamento> list = new ArrayList<>();
            Map<Integer, Area> map = new HashMap<>();
            
            while (rs.next()) {
                Area areaObj = map.get(rs.getInt("id_area"));
                
                if (areaObj == null) {
                    areaObj = instantiateArea(rs);
                    map.put(rs.getInt("id_area"), areaObj);
                }
                
                Equipamento obj = instantiateEquipamento(rs, areaObj);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Equipamento instantiateEquipamento(ResultSet rs, Area area) throws SQLException {
        Equipamento obj = new Equipamento();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Nome"));
        obj.setArea(area);
        return obj;
    }

    private Area instantiateArea(ResultSet rs) throws SQLException {
        Area area = new Area();
        area.setId(rs.getInt("id_area"));
        area.setName(rs.getString("AreaName"));
        return area;
    }
}
