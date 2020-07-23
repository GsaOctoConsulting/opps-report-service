package gov.gsa.service;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import gov.gsa.pojo.OppsData;
import gov.gsa.repoimpl.OppsImpl;



@RestController
@RequestMapping(value = "/opps/v1/")
public class OppsService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	OppsImpl oppsRepo;
	
	public void loadOppsData(boolean loadDelta)  {
		try {
			//downloadActiveFile();
			oppsRepo.loadOppsData(loadDelta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void downloadActiveFile() throws IOException {
		String activeOppsFile = "/Users/vinodcherukuri/Downloads/ContractOpportunitiesFullCSV.csv" ;
		String url = "https://s3.amazonaws.com/falextracts/Contract Opportunities/datagov/ContractOpportunitiesFullCSV.csv" ;
		InputStream in = new URL(url).openStream();
		Files.copy(in, Paths.get(activeOppsFile), StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * API to get opps by status
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @return
	 */
	public List<OppsData> getOppsByStatus(String startDate, String endDate, String orgname) {
		return oppsRepo.getOppsByStatus(startDate,endDate,orgname);
	}
	
	/**
	 * Api to get opps by volume
	 * @param startDate
	 * @param endDate
	 * @param orgname
	 * @return
	 */
	public List<OppsData> getOppsByPOPState(String startDate, String endDate, String orgname,String status, String isexport) {
		return oppsRepo.getOppsByPOPState(startDate,endDate,orgname,status,isexport);
	}
	
    /**
     * API to get opps by volume.
     * @param startDate
     * @param endDate
     * @param orgname
     * @return
     */
	public List<OppsData> getOppsByVolume(String startDate, String endDate, String orgname,String status, String isexport) {
		return oppsRepo.getOppsByVolume(startDate,endDate,orgname,status,isexport);
	}

	@Cacheable(cacheNames = "orglist", key = "{#level,#deptkey}" )
	public List<OppsData> getOrgs(String level, String deptkey) {
		final List<OppsData> fhOrgs = new ArrayList<OppsData>();
		try  {
			String strurl = "https://api.sam.gov/prod/federalorganizations/v1/orgs?limit=100&offset=0&api_key=SYzSGUk5nU0cZVsFYMSFRwzmTVjyI13b60RPYvq7";
			if(level != null && level.trim() != null ) {
				strurl = strurl + "&level="+level;
			} else {
				strurl = strurl + "&level=1";
			}
			
			if(deptkey != null && deptkey.trim() != null ) {
				strurl = strurl + "&fhorgid="+deptkey;
			}
			
			ResponseEntity<JsonNode> response = restTemplate.getForEntity(strurl , JsonNode.class);
			System.out.println(response.toString());
			JsonNode jn = response.getBody().get("orglist");
			
			if (jn.isArray()) {
			      for (JsonNode jsonNode : jn) {
			        OppsData data = new OppsData();
					data.setKey(jsonNode.get("fhorgname").asText());
					data.setCount(jsonNode.get("fhorgid").asText());
					fhOrgs.add(data);
			      }
			  }
		}catch (Exception e) {
			e.printStackTrace();
		}
		return fhOrgs;
		
	}


	public List<OppsData> getOppsByClassficiationCode(String startDate, String endDate, String orgname,String status, String isexport) {
		List<OppsData> bypscList =  oppsRepo.getOppsByClassficiationCode(startDate,endDate,orgname,status,isexport);
		for(OppsData data : bypscList ) {
			data.setKey(pullPscData(data.getKey()));
		}
		return bypscList;
	}
	
	public List<OppsData> getByNaicscode(String startDate, String endDate, String orgname,String status, String isexport) {
		return oppsRepo.getOppsByNaicsCode(startDate,endDate,orgname,status,isexport);
	}
	public List<OppsData> getBySetASideCodes(String startDate, String endDate, String orgname,String status, String isexport) {
		return oppsRepo.getOppsBySetasidecode(startDate,endDate,orgname,status,isexport);
	}
	public List<OppsData> getBytype(String startDate, String endDate, String orgname,String status,String popstate, String isexport) {
		return oppsRepo.getBytype(startDate,endDate,orgname,status, popstate,isexport);
	}
	public List<OppsData> getOppsByYear(String startDate, String endDate, String orgname, String status, String isexport) {
		return oppsRepo.getOppsByYear(startDate,endDate,orgname,status,isexport);
	}
	
	@Cacheable(cacheNames = "psccodes",key = "#pscCode")
	private String pullPscData(String pscCode) {
		try {
			RestTemplate restTemplate =  new RestTemplate();
			String strurl = "https://api.sam.gov/prod/locationservices/v1/api/publicpscdetails?q="+pscCode+"&api_key=SYzSGUk5nU0cZVsFYMSFRwzmTVjyI13b60RPYvq7&searchBy=psc" ;
			ResponseEntity<JsonNode> response = restTemplate.getForEntity(strurl , JsonNode.class);
			return pscCode +" - "+ response.getBody().get("productServiceCodeList").get(0).get("pscName").textValue(); 
		}catch(Exception ex) {
			return pscCode;
		}
	}
   
}
