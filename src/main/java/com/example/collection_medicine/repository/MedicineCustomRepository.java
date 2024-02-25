package com.example.collection_medicine.repository;

import com.example.collection_medicine.domain.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MedicineCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    public void bulkSave(List<Medicine> medicines) {
        String sql = "INSERT INTO health.medicine" +
                "(company, product_name, efficacy," +
                "use_method, precautions_warning, caution, interaction, side_effect," +
                "storage_method, image)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Medicine medicine = medicines.get(i);
                        ps.setString(1, medicine.getCompany());
                        ps.setString(2, medicine.getProductName());
                        ps.setString(3, medicine.getEfficacy());
                        ps.setString(4, medicine.getUseMethod());
                        ps.setString(5, medicine.getPrecautionsWarning());
                        ps.setString(6, medicine.getCaution());
                        ps.setString(7, medicine.getInteraction());
                        ps.setString(8, medicine.getSideEffect());
                        ps.setString(9, medicine.getStorageMethod());
                        ps.setString(10, medicine.getImage());
                    }

                    @Override
                    public int getBatchSize() {
                        return medicines.size();
                    }
                });
    }

    public void bulkUpdate(List<Medicine> medicines) {
        String sql = "UPDATE health.medicine SET " +
                "efficacy = ?, " +
                "use_method = ?, " +
                "precautions_warning = ?, " +
                "caution = ?, " +
                "interaction = ?, " +
                "side_effect = ?, " +
                "storage_method = ?, " +
                "image = ? " +
                "WHERE company = ? AND product_name = ? ";

        jdbcTemplate.batchUpdate(
                sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Medicine medicine = medicines.get(i);
                        ps.setString(1, medicine.getEfficacy());
                        ps.setString(2, medicine.getUseMethod());
                        ps.setString(3, medicine.getPrecautionsWarning());
                        ps.setString(4, medicine.getCaution());
                        ps.setString(5, medicine.getInteraction());
                        ps.setString(6, medicine.getSideEffect());
                        ps.setString(7, medicine.getStorageMethod());
                        ps.setString(8, medicine.getImage());
                        ps.setString(9, medicine.getCompany());
                        ps.setString(10, medicine.getProductName());
                    }

                    @Override
                    public int getBatchSize() {
                        return medicines.size();
                    }
                });
    }
}
