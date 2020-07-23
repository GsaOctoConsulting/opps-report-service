package gov.gsa.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.gsa.pojo.OppsData;
import gov.gsa.service.OppsService;

/**
 * Opportunities Analytics Rest controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping(value = "opps/v1/")
@CrossOrigin(maxAge = 3600)
public class OppsController {
	
	@Autowired
	OppsService oppsService;
	
	@RequestMapping(value = "about", method = RequestMethod.GET)
	public ResponseEntity<String> about()  {
		return new ResponseEntity<String>("This is reports apps!", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public void loadOppsData(@RequestParam boolean loaddelta)  {
		oppsService.loadOppsData(loaddelta);
	}
	
	@RequestMapping(value = "bystatus", method = RequestMethod.GET)
	public ResponseEntity<List<OppsData>> getOppsByStatus(@RequestParam(name="startDate",required=true) String startDate, 
			  @RequestParam(name="endDate",required=true) String endDate,
			  @RequestParam(name="orgname",required=false) String orgname)  {
		List<OppsData> oppsbystatus = oppsService.getOppsByStatus(startDate,endDate,orgname);
		return new ResponseEntity<List<OppsData>>(oppsbystatus, HttpStatus.OK);
	}
	
	@RequestMapping(value = "byopptype", method = RequestMethod.GET)
	public ResponseEntity<List<OppsData>> getBytype(@RequestParam(name="startDate",required=false) String startDate, 
			  @RequestParam(name="endDate",required=false) String endDate, 
			  @RequestParam(name="orgname",required=false) String orgname,
			  @RequestParam(name="status",required=false) String status,
			  @RequestParam(name="popstate",required=false) String popstate,
			  @RequestParam(name="export",required=false) String isexport)  {
		List<OppsData> oppsbystatus = oppsService.getBytype(startDate,endDate,orgname,status,popstate,isexport);
		return new ResponseEntity<List<OppsData>>(oppsbystatus, HttpStatus.OK);
	}
	
	@RequestMapping(value = "bypopstate", method = RequestMethod.GET)
	public ResponseEntity<List<OppsData>> getOppsByPOPState(@RequestParam(name="startDate",required=false) String startDate, 
			  @RequestParam(name="endDate",required=false) String endDate,
			  @RequestParam(name="orgname",required=false) String orgname,
			  @RequestParam(name="status",required=false) String status,
			  @RequestParam(name="export",required=false) String isexport)  {
		List<OppsData> oppsbystatus = oppsService.getOppsByPOPState(startDate,endDate,orgname,status,isexport);
		return new ResponseEntity<List<OppsData>>(oppsbystatus, HttpStatus.OK);
	}
	
	@RequestMapping(value = "byvolume", method = RequestMethod.GET)
	public ResponseEntity<List<OppsData>> getOppsByVolume(@RequestParam(name="startDate",required=false) String startDate, 
			  @RequestParam(name="endDate",required=false) String endDate,
			  @RequestParam(name="orgname",required=false) String orgname,
			  @RequestParam(name="status",required=false) String status,
			  @RequestParam(name="export",required=false) String isexport )  {
		List<OppsData> oppsbystatus = oppsService.getOppsByVolume(startDate,endDate,orgname,status,isexport);
		return new ResponseEntity<List<OppsData>>(oppsbystatus, HttpStatus.OK);
	}
	
	@RequestMapping(value = "byclassificationcode", method = RequestMethod.GET)
	public ResponseEntity<List<OppsData>> byclassificationcode(@RequestParam(name="startDate",required=false) String startDate, 
			  @RequestParam(name="endDate",required=false) String endDate,
			  @RequestParam(name="orgname",required=false) String orgname,
			  @RequestParam(name="status",required=false) String status,@RequestParam(name="export",required=false) String isexport )  {
		List<OppsData> oppsbystatus = oppsService.getOppsByClassficiationCode(startDate,endDate,orgname,status,isexport);
		return new ResponseEntity<List<OppsData>>(oppsbystatus, HttpStatus.OK);
	}
	
	@RequestMapping(value = "bynaicscode", method = RequestMethod.GET)
	public ResponseEntity<List<OppsData>> bynaicscode(@RequestParam(name="startDate",required=false) String startDate, 
			  @RequestParam(name="endDate",required=false) String endDate, 
			  @RequestParam(name="orgname",required=false) String orgname,
			  @RequestParam(name="status",required=false) String status,@RequestParam(name="export",required=false) String isexport )  {
		List<OppsData> oppsbystatus = oppsService.getByNaicscode(startDate,endDate,orgname,status,isexport);
		return new ResponseEntity<List<OppsData>>(oppsbystatus, HttpStatus.OK);
	}
	
	@RequestMapping(value = "bysetasidecode", method = RequestMethod.GET)
	public ResponseEntity<List<OppsData>> bysetasidecode(@RequestParam(name="startDate",required=false) String startDate, 
			  @RequestParam(name="endDate",required=false) String endDate,
			  @RequestParam(name="orgname",required=false) String orgname,
			  @RequestParam(name="status",required=false) String status,
			  @RequestParam(name="export",required=false) String isexport )  {
		List<OppsData> oppsbystatus = oppsService.getBySetASideCodes(startDate,endDate,orgname,status,isexport);
		return new ResponseEntity<List<OppsData>>(oppsbystatus, HttpStatus.OK);
	}
	
	@RequestMapping(value = "byyear", method = RequestMethod.GET)
	public ResponseEntity<List<OppsData>> byYear(@RequestParam(name="startDate",required=false) String startDate, 
			  @RequestParam(name="endDate",required=false) String endDate,
			  @RequestParam(name="orgname",required=false) String orgname,
			  @RequestParam(name="status",required=false) String status,@RequestParam(name="export",required=false) String isexport )  {
		List<OppsData> oppsbystatus = oppsService.getOppsByYear(startDate,endDate,orgname,status,isexport);
		return new ResponseEntity<List<OppsData>>(oppsbystatus, HttpStatus.OK);
	}
	
	@RequestMapping(value = "getorgs", method = RequestMethod.GET)
	public ResponseEntity<List<OppsData>> getOrgs(@RequestParam(name="level", defaultValue = "1" ) String level, 
			@RequestParam(name="deptkey",required = false) String deptkey)  {
		List<OppsData> oppsbystatus = oppsService.getOrgs(level,deptkey);
		return new ResponseEntity<List<OppsData>>(oppsbystatus, HttpStatus.OK);
	}
	
}
