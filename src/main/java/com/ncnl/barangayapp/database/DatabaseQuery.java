package com.ncnl.barangayapp.database;

import com.ncnl.barangayapp.model.Resident;
import com.ncnl.barangayapp.mapper.ResidentMapper;
import com.ncnl.barangayapp.repository.CrudMethod;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DatabaseQuery implements CrudMethod<Resident> {

    DatabaseManager manager = DatabaseManager.getInstance();

    String INSERT_RESIDENT = "INSERT INTO residents (id, fullname, sex, age, location, category, additional_info, status, timestamp) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String READ_ALL = "SELECT * FROM residents";
    String READ_RESIDENT = "SELECT * FROM residents where id = ?";
    String READ_ALL_FROM_BIN = "SELECT * FROM resident_bin";
    String EXIST_IN_BIN = "SELECT COUNT(*) FROM resident_bin WHERE id = ?";
    String EXIST_IN_MAIN = "SELECT COUNT(*) FROM residents WHERE id = ?";

    String UPDATE_RESIDENT = "UPDATE residents SET fullname = ?, sex = ?, age = ?, location = ?, " +
            "category = ?, additional_info = ?, status = ?, timestamp = ? WHERE id = ?";

    String DELETE_RESIDENT = "DELETE FROM residents WHERE id = ?";
    String PERMANENT_DELETE_RESIDENT = "DELETE FROM resident_bin WHERE id = ?";
    String UPDATE_STATUS = "UPDATE residents SET status = ? WHERE id = ?";
    String MOVE_RESIDENT_TO_BIN = """
                INSERT INTO resident_bin (id, fullname, sex, age, location, category, additional_info, status, timestamp)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;


    @Override
    public Optional<Resident> create(Resident object) {
        return Optional.ofNullable(
                manager.withConnection(conn -> {
                    try (PreparedStatement psmt = conn.prepareStatement(INSERT_RESIDENT)) {

                        psmt.setString(1, object.getId().toString());
                        psmt.setString(2, object.getFullname());
                        psmt.setString(3, object.getSex());
                        psmt.setInt(4, object.getAge());
                        psmt.setString(5, object.getLocation());
                        psmt.setString(6, object.getCategory());
                        psmt.setString(7, object.getAdditional_info());
                        psmt.setString(8, object.getStatus());
                        psmt.setString(9, object.getTimestamp());

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
                psmt.setString(1, newData.getFullname());
                psmt.setString(2, newData.getSex());
                psmt.setInt(3, newData.getAge());
                psmt.setString(4, newData.getLocation());
                psmt.setString(5, newData.getCategory());
                psmt.setString(6, newData.getAdditional_info());
                psmt.setString(7, newData.getStatus());
                psmt.setTimestamp(8, Timestamp.valueOf(newData.getTimestamp()));
                psmt.setString(9, id.toString());

                int rows = psmt.executeUpdate();
                return rows > 0;
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

                int rows = psmt.executeUpdate();
                return rows > 0;
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

    public void moveToBin(Resident resident) {
        manager.withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(MOVE_RESIDENT_TO_BIN)) {
                stmt.setString(1, resident.getId().toString());
                stmt.setString(2, resident.getFullname());
                stmt.setString(3, resident.getSex());
                stmt.setInt(4, resident.getAge());
                stmt.setString(5, resident.getLocation());
                stmt.setString(6, resident.getCategory());
                stmt.setString(7, resident.getAdditional_info());
                stmt.setString(8, resident.getStatus());
                stmt.setString(9, resident.getTimestamp());

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
