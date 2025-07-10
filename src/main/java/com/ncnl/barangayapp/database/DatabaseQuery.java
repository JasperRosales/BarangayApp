package com.ncnl.barangayapp.database;

import com.ncnl.barangayapp.model.Resident;
import com.ncnl.barangayapp.mapper.ResidentMapper;
import com.ncnl.barangayapp.repository.CrudMethod;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseQuery implements CrudMethod<Resident> {

    DatabaseManager manager = DatabaseManager.getInstance();

    String INSERT_RESIDENT = "INSERT INTO residents (id, lastname, firstname, middlename, qualifier, number, street_name, location, place_of_birth, date_of_birth, age, sex, civil_status, citizenship, occupation, relationship_to_household_head, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String READ_ALL = "SELECT * FROM residents";
    String READ_RESIDENT = "SELECT * FROM residents where id = ?";
    String READ_ALL_FROM_BIN = "SELECT * FROM resident_bin";
    String EXIST_IN_BIN = "SELECT COUNT(*) FROM resident_bin WHERE id = ?";
    String EXIST_IN_MAIN = "SELECT COUNT(*) FROM residents WHERE id = ?";

    String UPDATE_RESIDENT = "UPDATE residents SET lastname = ?, firstname = ?, middlename = ?, qualifier = ?, number = ?, street_name = ?, location = ?, place_of_birth = ?, date_of_birth = ?, age = ?, sex = ?, civil_status = ?, citizenship = ?, occupation = ?, relationship_to_household_head = ?, status = ? WHERE id = ?";

    String DELETE_RESIDENT = "DELETE FROM residents WHERE id = ?";
    String PERMANENT_DELETE_RESIDENT = "DELETE FROM resident_bin WHERE id = ?";
    String UPDATE_STATUS = "UPDATE residents SET status = ? WHERE id = ?";
    String MOVE_RESIDENT_TO_BIN = """
                INSERT INTO resident_bin (id, lastname, firstname, middlename, qualifier, number, street_name, location, place_of_birth, date_of_birth, age, sex, civil_status, citizenship, occupation, relationship_to_household_head, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";

    @Override
    public Optional<Resident> create(Resident object) {
        return Optional.ofNullable(
                manager.withConnection(conn -> {
                    try (PreparedStatement psmt = conn.prepareStatement(INSERT_RESIDENT)) {

                        psmt.setString(1, object.getId().toString());
                        psmt.setString(2, object.getLastname());
                        psmt.setString(3, object.getFirstname());
                        psmt.setString(4, object.getMiddlename());
                        psmt.setString(5, object.getQualifier());
                        psmt.setString(6, object.getNumber());
                        psmt.setString(7, object.getStreetName());
                        psmt.setString(8, object.getLocation());
                        psmt.setString(9, object.getPlaceOfBirth());
                        psmt.setString(10, object.getDateOfBirth());
                        psmt.setInt(11, object.getAge());
                        psmt.setString(12, object.getSex());
                        psmt.setString(13, object.getCivilStatus());
                        psmt.setString(14, object.getCitizenship());
                        psmt.setString(15, object.getOccupation());
                        psmt.setString(16, object.getRelationshipToHouseHoldHead());
                        psmt.setString(17, object.getStatus());

                        int rows = psmt.executeUpdate();
                        if (rows > 0) {
                            return ResidentMapper.toInsertedResident(object);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
        );
    }

    public ArrayList<Resident> readAllResidents() {
        return manager.withConnection(conn -> {
            try (PreparedStatement psmt = conn.prepareStatement(READ_ALL);
                 ResultSet rs = psmt.executeQuery()) {

                ArrayList<Resident> list = new ArrayList<>();
                while (rs.next())
                    list.add(ResidentMapper.mapResultSetToResident(rs));
                return list;

            } catch (SQLException e) {
                throw new RuntimeException("Failed to read all residents", e);
            }
        });
    }

    @Override
    public Optional<Resident> read(UUID id) {
        return Optional.ofNullable(
                manager.withConnection(conn -> {
                    try (PreparedStatement psmt = conn.prepareStatement(READ_RESIDENT)) {
                        psmt.setString(1, id.toString());
                        try (ResultSet rs = psmt.executeQuery()) {
                            if (rs.next()) {
                                return ResidentMapper.mapResultSetToResident(rs);
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException("Read failed", e);
                    }
                    return null;
                })
        );
    }

    public boolean existsInBin(String residentId) {
        return manager.withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(EXIST_IN_BIN)) {
                stmt.setString(1, residentId);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            }
        });
    }

    public boolean existsInMain(String id) {
        return manager.withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(EXIST_IN_MAIN)) {
                stmt.setString(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            }
        });
    }

    @Override
    public boolean update(UUID id, Resident newData) {
        return manager.withConnection(conn -> {
            try (PreparedStatement psmt = conn.prepareStatement(UPDATE_RESIDENT)) {
                psmt.setString(1, newData.getLastname());
                psmt.setString(2, newData.getFirstname());
                psmt.setString(3, newData.getMiddlename());
                psmt.setString(4, newData.getQualifier());
                psmt.setString(5, newData.getNumber());
                psmt.setString(6, newData.getStreetName());
                psmt.setString(7, newData.getLocation());
                psmt.setString(8, newData.getPlaceOfBirth());
                psmt.setString(9, newData.getDateOfBirth());
                psmt.setInt(10, newData.getAge());
                psmt.setString(11, newData.getSex());
                psmt.setString(12, newData.getCivilStatus());
                psmt.setString(13, newData.getCitizenship());
                psmt.setString(14, newData.getOccupation());
                psmt.setString(15, newData.getRelationshipToHouseHoldHead());
                psmt.setString(16, newData.getStatus());
                psmt.setString(17, id.toString());

                return psmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("Update failed", e);
            }
        });
    }

    public boolean updateResidentStatus(UUID id, String status) {
        return manager.withConnection(conn -> {
            try (PreparedStatement psmt = conn.prepareStatement(UPDATE_STATUS)) {
                psmt.setString(1, status);
                psmt.setString(2, id.toString());
                return psmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("Status update failed", e);
            }
        });
    }

    @Override
    public boolean delete(UUID id) {
        return manager.withConnection(conn -> {
            try (PreparedStatement psmt = conn.prepareStatement(DELETE_RESIDENT)) {
                psmt.setString(1, id.toString());
                return psmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("Delete failed", e);
            }
        });
    }

    public List<Resident> readAllFromBin() {
        List<Resident> binResidents = new ArrayList<>();
        return DatabaseManager.getInstance().withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(READ_ALL_FROM_BIN);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Resident resident = ResidentMapper.mapResultSetToResident(rs);
                    binResidents.add(resident);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return binResidents;
        });
    }

    public void moveToBin(Resident r) {
        manager.withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(MOVE_RESIDENT_TO_BIN)) {
                stmt.setString(1, r.getId().toString());
                stmt.setString(2, r.getLastname());
                stmt.setString(3, r.getFirstname());
                stmt.setString(4, r.getMiddlename());
                stmt.setString(5, r.getQualifier());
                stmt.setString(6, r.getNumber());
                stmt.setString(7, r.getStreetName());
                stmt.setString(8, r.getLocation());
                stmt.setString(9, r.getPlaceOfBirth());
                stmt.setString(10, r.getDateOfBirth());
                stmt.setInt(11, r.getAge());
                stmt.setString(12, r.getSex());
                stmt.setString(13, r.getCivilStatus());
                stmt.setString(14, r.getCitizenship());
                stmt.setString(15, r.getOccupation());
                stmt.setString(16, r.getRelationshipToHouseHoldHead());
                stmt.setString(17, r.getStatus());
                stmt.executeUpdate();
            }
            return null;
        });
    }

    public void deleteFromBin(UUID residentId) {
        manager.withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(PERMANENT_DELETE_RESIDENT)) {
                stmt.setString(1, residentId.toString());
                stmt.executeUpdate();
            }
            return null;
        });
    }
}
