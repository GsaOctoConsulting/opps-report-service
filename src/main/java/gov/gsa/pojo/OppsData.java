package gov.gsa.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OppsData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String key;
	private String noticeid;
	public String getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSolnum() {
		return solnum;
	}

	public void setSolnum(String solnum) {
		this.solnum = solnum;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getSubtier() {
		return subtier;
	}

	public void setSubtier(String subtier) {
		this.subtier = subtier;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getPosteddate() {
		return posteddate;
	}

	public void setPosteddate(String posteddate) {
		this.posteddate = posteddate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPopstate() {
		return popstate;
	}

	public void setPopstate(String popstate) {
		this.popstate = popstate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSetasidecode() {
		return setasidecode;
	}

	public void setSetasidecode(String setasidecode) {
		this.setasidecode = setasidecode;
	}

	public String getClassificationcode() {
		return classificationcode;
	}

	public void setClassificationcode(String classificationcode) {
		this.classificationcode = classificationcode;
	}

	public String getNaicscode() {
		return naicscode;
	}

	public void setNaicscode(String naicscode) {
		this.naicscode = naicscode;
	}

	public String getSelflink() {
		return selflink;
	}

	public void setSelflink(String selflink) {
		this.selflink = selflink;
	}



	private String title;
	private String solnum;
	private String deptname;
	private String subtier;
	private String office;
	private String posteddate;
	private String type;
	private String popstate;
	private String status;
	private String setasidecode;
	private String classificationcode;
	private String naicscode;
	private String selflink;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	

	private String count;
	

	
}
