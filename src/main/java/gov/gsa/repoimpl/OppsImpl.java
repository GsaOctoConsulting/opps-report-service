package gov.gsa.repoimpl;


import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import gov.gsa.pojo.OppsData;



@RestController
@RequestMapping(value = "opps/v1/")
public class OppsImpl {
	
	Logger logger = LoggerFactory.getLogger(OppsImpl.class);
	
	private final String TABLE_NAME = "public.opps_notice_new  where 1=1 ";
	private final String STATUS_ARCHIVE = " ";
	
	private final String REPORT_QUERY =" select noticeid,title,solnum,deptname,subtier," + 
			"	office,posteddate,type,popstate,status,setasidecode,classificationcode,naicscode,selflink " + 
			"	from public.opps_notice_new where 1=1 " ;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;
	
	public void loadOppsData(boolean loaddelta)  {
		FileReader filereader = null;
		try {
		   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		   File file = new File("C:/Users/Administrator/Downloads/FY2017_archived_opportunities.csv");
		   int count=0;
		   filereader = new FileReader(file); 
	       String[] nextRecord; 
	       Date today =  new Date();
	       Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(filereader);
	       for (CSVRecord record : records) {  
	        	try {
	        		//String isLatest = isLatestOpp(getVal(record.get(0)));
	        		String isLatest = "A";
		        	Date rptposteddate =	dateFormat.parse(record.get(6));
		        	String datestr = dateFormat.format(dateFormat.parse(record.get(6)));
		        	if((loaddelta && rptposteddate.compareTo(today) == 0) || !loaddelta) {
		            	jdbcTemplate.update("insert into "+TABLE_NAME+"(noticeId, title , solnum,"
		            			+ " deptname,subtier,office,posteddate,type,archivetype,setasidecode,naicscode,"
		            			+ " classificationcode,popstate,status,officetype,state,selflink,islatest )"
		            		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 
		            		getVal(record.get(0)), getVal(record.get(1)),getVal(record.get(2)),getVal(record.get(3)),
		            		getVal(record.get(4)),getVal(record.get(5)),
		            		datestr,getVal(record.get(7)),getVal(record.get(9)),getVal(record.get(11)),getVal(record.get(14)),getVal(record.get(15)),
		            		getVal(record.get(18)),
		            		getStatus(getVal(record.get(21)),isLatest),getVal(record.get(36)),getVal(record.get(37)),getVal(record.get(42)),isLatest );
		        	}
	        	}catch(Exception ex) {
	        		logger.error("error while processing record"+record.get(0),ex);
	        		if(count > 0) {
	        			throw ex;
	        		}
	        	}
	        	System.out.println(" Processing  "+count);
	        	count++;
	        }
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				filereader.close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	
	public String isLatestOpp(String noticeId) {
		try {
			RestTemplate restTemplate =  new RestTemplate();
			String strurl = "https://beta.sam.gov/api/prod/opps/v2/opportunities/"+noticeId+"?random="+Math.random() ;
			ResponseEntity<JsonNode> response = restTemplate.getForEntity(strurl , JsonNode.class);
			if(response != null && response.getBody() != null) {
				boolean islatest = response.getBody().get("latest").booleanValue();
				boolean archived = response.getBody().get("archived").booleanValue();
				boolean cancelled = response.getBody().get("cancelled").booleanValue();
				if (islatest & !archived) {
					return "1";
				}else if (archived && !cancelled) {
					return "A";
				} else if (cancelled) {
					return "C";
				} else {
					return "0";
				}
			} else return "0";
		}catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
			//return "0";
		}
	}
	
	
	
	private String getStatus(String str,String isLatest) {
		if("1".equalsIgnoreCase(isLatest) && "yes".equalsIgnoreCase(str)) {
			return "Active Latest" ;
		} else if("yes".equalsIgnoreCase(str)) {
			return "Active" ;
		} else if("no".equalsIgnoreCase(str) 
				 || "A".equalsIgnoreCase(str) || "0".equalsIgnoreCase(str)) {
			return "Archived" ;
		} else if("C".equalsIgnoreCase(str)) {
			return "Cancelled" ;
		}
		return str; 
	}
	
	private String getVal(String val) {
		if(val != null) {
			return val.toString();
		} else {
			return null;
		}
	}

	/**
	 * API to retrieve opps by status
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @return
	 */
	public List<OppsData> getOppsByStatus(String startDate, String endDate,  String orgname) {
		String query = " select status as key, count(*) as count from "+TABLE_NAME ;
				query = applyFilters(startDate, endDate, orgname, null, query);
		query = query + " group by status ";
		logger.info("by status query >> "+query);
		return jdbcTemplate.query(query, new OppsRowMapper());
	}

	/**
	 * API to retrieve opps by status
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @return
	 */
	public List<OppsData> getOppsByPOPState(String startDate, String endDate, String orgname, String status, String isexport) {
			String query = " select popstate as key, count(*) as count from "+TABLE_NAME  ;
					query = applyFilters(startDate, endDate, orgname, status, query);
			query = query + " group by popstate ";
			logger.info("by popstate query >> "+query);
			return jdbcTemplate.query(query, new OppsRowMapper());
	}

	/**
	 * aPI to get opps by volume
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @return
	 */
	public List<OppsData> getOppsByVolume(String startDate, String endDate, String orgname, String status, String isexport) {
		String query = " select case when  length(office)=0   then 'Non-USA' else office || ' From '|| deptname end as key, count(*) "
				+ "  as count from "+TABLE_NAME  ;
				query = applyFilters(startDate, endDate, orgname, status, query);
			query = query + " group by key order  by count desc limit 10";
			logger.info("by volume query >> "+query);
			return jdbcTemplate.query(query, new OppsRowMapper());
	}


	/**
	 * ApI to  get data by classification code 
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @param status
	 * @param isexport
	 * @return
	 */
	public List<OppsData> getOppsByClassficiationCode(String startDate, String endDate, String orgname, String status, String isexport) {
		String query = " select classificationcode as key, count(*) as count from "+TABLE_NAME;
				query = applyFilters(startDate, endDate, orgname, status, query);
			query = query + " and length(classificationcode) > 0  group by classificationcode order  by count desc limit 10";
			logger.info("by classification code query >> "+query);
			return jdbcTemplate.query(query, new OppsRowMapper());
	}
	
	/**
	 * API to get data by naics code
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @param status
	 * @param isexport
	 * @return
	 */
	public List<OppsData> getOppsByNaicsCode(String startDate, String endDate, String orgname, String status, String isexport) {
		String query = " select naicscode as key, count(*) as count from "+TABLE_NAME;
				query = applyFilters(startDate, endDate, orgname, status, query);
			query = query + " and length(naicscode) > 0  group by naicscode order  by count desc limit 10";
			logger.info("by naics code query >> "+query);
			return jdbcTemplate.query(query, new OppsRowMapper());
	}
	
	/**
	 * API to get data by set a side code
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @param status
	 * @param isexport
	 * @return
	 */
	public List<OppsData> getOppsBySetasidecode(String startDate, String endDate, String orgname, String status, String isexport) {
		String query = " select setasidecode as key, count(*) as count from "+TABLE_NAME ;
		    query = applyFilters(startDate, endDate, orgname, status, query);
			query = query + " and length(setasidecode) > 0  group by setasidecode order  by count desc limit 10";
			logger.info("by setaside query >> "+query);
			return jdbcTemplate.query(query, new OppsRowMapper());
	}

	/**
	 * API to get data by type of opportunity
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @param status
	 * @param isexport
	 * @return
	 */
	public List<OppsData> getBytype(String startDate, String endDate, String orgname,String status,String popstate, String isexport) {
		
		String query = " select type as key, count(*) as count from "+TABLE_NAME ;
				query = applyFilters(startDate, endDate, orgname, status, query);
				if(org.apache.commons.lang3.StringUtils.isNotBlank(popstate)) {
					query = query + " and popstate = '"+popstate+"' " ;
				}
				query = query + " and length(type) > 0  group by type order  by count desc limit 10 ";
				logger.info("by type  query >> "+query);
			if(org.apache.commons.lang3.StringUtils.isNotBlank(isexport) && "Yes".equalsIgnoreCase(isexport)){
				logger.info("by type report query >> "+REPORT_QUERY + " and type in ( select key from ( " + query + ") res ) " ); 
				return jdbcTemplate.query(REPORT_QUERY + " and type in ( select key from ( " + query + " ) res ) limit 1000 ", new OppsRowMapperExport());
			} else {
				return jdbcTemplate.query(query, new OppsRowMapper());
			}
	}
	
	/**
	 * API to get data volume by year
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @param status
	 * @param isexport
	 * @return
	 */
	public List<OppsData> getOppsByYear(String startDate, String endDate, String orgname, String status, String isexport) {
		String query = " select EXTRACT(YEAR FROM posteddate::date) as key, count(*) as count from "+TABLE_NAME ;
			query = applyFilters(startDate, endDate, orgname, status, query);
			query = query + " group by key order  by key desc " ;
			logger.info("by year query >> "+query);
			return jdbcTemplate.query(query, new OppsRowMapper());
	}


	private String applyFilters(String startDate, String endDate, String orgname, String status, String query) {
		if(!StringUtils.isEmpty(startDate)) {
			query = query + " and posteddate::date >= '"+startDate+"'::date ";
		}
		if(!StringUtils.isEmpty(startDate)) {
			query = query + " and posteddate::date <= '"+endDate+"'::date ";
		}
		if(!StringUtils.isEmpty(orgname)) {
			query = query + " and (deptname ilike '"+orgname+"' OR subtier ilike '"+orgname+"' OR office ilike '"+orgname+"') ";
		}
		if(!StringUtils.isEmpty(status)) {
			query = query + " and status ='"+status+"' ";
		}
		return query;
	}

	
   
}
