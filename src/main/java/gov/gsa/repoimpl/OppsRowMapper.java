package gov.gsa.repoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gov.gsa.pojo.OppsData;

public class OppsRowMapper implements RowMapper<OppsData> {

	@Override
	public OppsData mapRow(ResultSet rs, int rowNum) throws SQLException {
		OppsData oppsdata = new OppsData();
		oppsdata.setCount(rs.getString("count"));
		oppsdata.setKey(rs.getString("key"));
		return oppsdata;
	}
}
