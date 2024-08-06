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
import model.dao.MotorDao;
import model.entities.Area;
import model.entities.Equipamento;
import model.entities.Motor;

public class MotorDaoJDBC implements MotorDao {

    private Connection conn;
    
    public MotorDaoJDBC(Connection conn) {
        this.conn = conn;
    }
    
    @Override
    public void insert(Motor obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "INSERT INTO motor "
                + "(Tensao, Corrente, Rotacao, Carcaca, FatorPotencia, FatorServico, Fabricante, CodigoSap, PotenciaWatts, GrauProtecao, Frequencia, RolamentoDianteiro, RolamentoTraseiro, id_area, id_equipamento) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
            
            st.setString(1, obj.getTensao());
            st.setString(2, obj.getCorrente());
            //st.setString(3, obj.getPotenciaCv());
            st.setString(4, obj.getRotacao());
            st.setString(5, obj.getCarcaca());
            st.setString(6, obj.getFatorPotencia());
            st.setString(7, obj.getFatorServico());
            st.setString(8, obj.getFabricante());
            st.setString(9, obj.getCodigoSap());
            st.setString(10, obj.getPotenciaWatts());
            st.setString(11, obj.getGrauProtecao());
            st.setString(12, obj.getFrequencia());
            st.setString(13, obj.getRolamentoDianteiro());
            st.setString(14, obj.getRolamentoTraseiro());
            st.setInt(15, obj.getArea().getId());
            st.setInt(16, obj.getEquipamento().getId());
            
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
    public void update(Motor obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "UPDATE motor "
                + "SET Tensao = ?, Corrente = ?, Rotacao = ?, Carcaca = ?, FatorPotencia = ?, FatorServico = ?, Fabricante = ?, CodigoSap = ?, PotenciaWatts = ?, GrauProtecao = ?, Frequencia = ?, RolamentoDianteiro = ?, RolamentoTraseiro = ?, id_area = ?, id_equipamento = ? "
                + "WHERE Id = ?");
            
            st.setString(1, obj.getTensao());
            st.setString(2, obj.getCorrente());
            //st.setString(3, obj.getPotenciaCv());
            st.setString(4, obj.getRotacao());
            st.setString(5, obj.getCarcaca());
            st.setString(6, obj.getFatorPotencia());
            st.setString(7, obj.getFatorServico());
            st.setString(8, obj.getFabricante());
            st.setString(9, obj.getCodigoSap());
            st.setString(10, obj.getPotenciaWatts());
            st.setString(11, obj.getGrauProtecao());
            st.setString(12, obj.getFrequencia());
            st.setString(13, obj.getRolamentoDianteiro());
            st.setString(14, obj.getRolamentoTraseiro());
            st.setInt(15, obj.getArea().getId());
            st.setInt(16, obj.getEquipamento().getId());
            st.setInt(17, obj.getId());
            
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
            st = conn.prepareStatement("DELETE FROM motor WHERE Id = ?");
            
            st.setInt(1, id);
            
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Motor findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                "SELECT motor.*, area.Name as AreaName, equipamento.Name as EquipamentoName "
                + "FROM motor "
                + "INNER JOIN area ON motor.id_area = area.Id "
                + "INNER JOIN equipamento ON motor.id_equipamento = equipamento.Id "
                + "WHERE motor.Id = ?");
            
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Area area = instantiateArea(rs);
                Equipamento equipamento = instantiateEquipamento(rs);
                Motor obj = instantiateMotor(rs, area, equipamento);
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
    public List<Motor> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                "SELECT motor.*, area.Nome as AreaName, equipamento.Nome as EquipamentoName "
                + "FROM motor "
                + "INNER JOIN area ON motor.id_area = area.Id "
                + "INNER JOIN equipamento ON motor.id_equipamento = equipamento.Id "
                + "ORDER BY motor.Fabricante");
            
            rs = st.executeQuery();
            
            List<Motor> list = new ArrayList<>();
            Map<Integer, Area> areaMap = new HashMap<>();
            Map<Integer, Equipamento> equipamentoMap = new HashMap<>();
            
            while (rs.next()) {
                Area area = areaMap.get(rs.getInt("id_area"));
                if (area == null) {
                    area = instantiateArea(rs);
                    areaMap.put(rs.getInt("id_area"), area);
                }
                
                Equipamento equipamento = equipamentoMap.get(rs.getInt("id_equipamento"));
                if (equipamento == null) {
                    equipamento = instantiateEquipamento(rs);
                    equipamentoMap.put(rs.getInt("id_equipamento"), equipamento);
                }
                
                Motor obj = instantiateMotor(rs, area, equipamento);
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

    private Motor instantiateMotor(ResultSet rs, Area area, Equipamento equipamento) throws SQLException {
        Motor obj = new Motor();
        obj.setId(rs.getInt("Id"));
        obj.setTensao(rs.getString("Tensao"));
        obj.setCorrente(rs.getString("Corrente"));
        //obj.setPotenciaCv(rs.getString("Potencia_cv"));
        obj.setRotacao(rs.getString("Rotacao"));
        obj.setCarcaca(rs.getString("Carcaca"));
        obj.setFatorPotencia(rs.getString("Fator_potencia"));
        obj.setFatorServico(rs.getString("Fator_servico"));
        obj.setFabricante(rs.getString("Fabricante"));
        obj.setCodigoSap(rs.getString("Codigo_sap"));
        obj.setPotenciaWatts(rs.getString("Potencia_watts"));
        obj.setGrauProtecao(rs.getString("Grau_protecao"));
        obj.setFrequencia(rs.getString("Frequencia"));
        obj.setRolamentoDianteiro(rs.getString("Rolamento_dianteiro"));
        obj.setRolamentoTraseiro(rs.getString("Rolamento_traseiro"));
        obj.setArea(area);
        obj.setEquipamento(equipamento);
        return obj;
    }

    private Area instantiateArea(ResultSet rs) throws SQLException {
        Area area = new Area();
        area.setId(rs.getInt("id_area"));
        area.setName(rs.getString("AreaName"));
        return area;
    }

    private Equipamento instantiateEquipamento(ResultSet rs) throws SQLException {
        Equipamento equipamento = new Equipamento();
        equipamento.setId(rs.getInt("id_equipamento"));
        equipamento.setName(rs.getString("EquipamentoName"));
        return equipamento;
    }
    
    @Override
    public List<Motor> findByArea(Area area) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                "SELECT motor.*, area.Name as AreaName, equipamento.Name as EquipamentoName "
                + "FROM motor "
                + "INNER JOIN area ON motor.id_area = area.Id "
                + "INNER JOIN equipamento ON motor.id_equipamento = equipamento.Id "
                + "WHERE id_area = ? "
                + "ORDER BY motor.Fabricante");
            
            st.setInt(1, area.getId());
            rs = st.executeQuery();
            
            List<Motor> list = new ArrayList<>();
            Map<Integer, Area> areaMap = new HashMap<>();
            Map<Integer, Equipamento> equipamentoMap = new HashMap<>();
            
            while (rs.next()) {
                Area areaObj = areaMap.get(rs.getInt("id_area"));
                if (areaObj == null) {
                    areaObj = instantiateArea(rs);
                    areaMap.put(rs.getInt("id_area"), areaObj);
                }
                
                Equipamento equipamento = equipamentoMap.get(rs.getInt("id_equipamento"));
                if (equipamento == null) {
                    equipamento = instantiateEquipamento(rs);
                    equipamentoMap.put(rs.getInt("id_equipamento"), equipamento);
                }
                
                Motor obj = instantiateMotor(rs, areaObj, equipamento);
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
}
