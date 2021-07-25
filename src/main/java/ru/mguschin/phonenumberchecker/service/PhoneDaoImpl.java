package ru.mguschin.phonenumberchecker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

@Repository
public class PhoneDaoImpl implements PhoneDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PhoneDaoImpl (DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public String phoneCheck(String phone, String requestId) {

        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("p1", phone).addValue("p2", phone.substring(1));

        String query = "select count(*) as RESULT from ((select 1 as RESULT from TABLE1 where PHONE = :p1 limit 1) union all (select 1 from TABLE2 where PHONE = :p2 limit 1)) as t";

        Integer res = jdbcTemplate.queryForObject(query, namedParameters, Integer.class);

        switch (res.intValue()) {
            case 0: return "ACCEPT";
            case 1: return "CHALLENGE";
            default: return "DECLINE";
        }
    }
}
