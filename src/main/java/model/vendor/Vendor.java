package model.vendor;

import java.util.Date;
import java.util.List;

public class Vendor {

	private String sourceSystem;
	private String sourceSystemId;
	private String accountNumber;
	private String industryKey;
	private Date createdDate;
	private String vendorAccountGroup;
	private String name1;
	
	public String getVendorAccountGroup() {
		return vendorAccountGroup;
	}

	public void setVendorAccountGroup(String vendorAccountGroup) {
		this.vendorAccountGroup = vendorAccountGroup;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	private List<VendorCompany> companies;

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSourceSystemId() {
		return sourceSystemId;
	}

	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getIndustryKey() {
		return industryKey;
	}

	public void setIndustryKey(String industryKey) {
		this.industryKey = industryKey;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<VendorCompany> getCompanies() {
		return companies;
	}

	public void setCompanies(List<VendorCompany> companies) {
		this.companies = companies;
	}
	
	
	

}
