package ru.mguschin.phonenumberchecker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PhoneDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PhoneDao (DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Integer phoneCheck(ListTable listTable, String phone, String requestId) {

        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("p1", phone);

        String query = "select count(*) from " + listTable.getTableName() + " where PHONE = :p1 limit 1";

        Integer res = jdbcTemplate.queryForObject(query, namedParameters, Integer.class);

        return res;
    }
}
