package ru.mguschin.phonenumberchecker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class LogDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public LogDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void logRequest(String phone, String requestId, CheckResult result) {

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("phone", phone)
                .addValue("requestid", requestId)
                .addValue("result", result.toString());

        String query = "insert into requestlog (phone, requestid, result) values (:phone, :requestid, :result)";

        jdbcTemplate.update(query, namedParameters);
    }
}
