package gov.gsa.repoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gov.gsa.pojo.OppsData;

public class OppsRowMapperExport implements RowMapper<OppsData> {

	@Override
	public OppsData mapRow(ResultSet rs, int rowNum) throws SQLException {
		OppsData oppsdata = new OppsData();
		oppsdata.setNoticeid(rs.getString("noticeid"));
		oppsdata.setTitle(rs.getString("title"));
		oppsdata.setSolnum(rs.getString("solnum"));
		oppsdata.setDeptname(rs.getString("deptname"));
		oppsdata.setSubtier(rs.getString("subtier"));
		oppsdata.setOffice(rs.getString("office"));
		oppsdata.setPosteddate(rs.getString("posteddate"));
		oppsdata.setType(rs.getString("type"));
		oppsdata.setPopstate(rs.getString("popstate"));
		oppsdata.setStatus(rs.getString("status"));
		oppsdata.setSetasidecode(rs.getString("setasidecode"));
		oppsdata.setClassificationcode(rs.getString("classificationcode"));
		oppsdata.setSelflink(rs.getString("selflink"));
		return oppsdata;
	}

}
