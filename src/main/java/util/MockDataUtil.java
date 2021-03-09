package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import model.vendor.Vendor;
import model.vendor.VendorCompany;

public class MockDataUtil {

	public static Vendor getVendor(int numberOfCompanies) {

		Vendor vendor = new Vendor();

		SapSystemsEnum[] sapSystems = SapSystemsEnum.values();
		SapSystemsEnum sapSystem = sapSystems[RandomUtils.nextInt(0, sapSystems.length)];

		vendor.setSourceSystem(sapSystem.name());
		vendor.setSourceSystemId(sapSystem.getSystemId());
		vendor.setAccountNumber(RandomStringUtils.randomNumeric(10));
		vendor.setIndustryKey(RandomStringUtils.randomNumeric(4));
		vendor.setCreatedDate(new Date());
		vendor.setVendorAccountGroup(RandomStringUtils.randomNumeric(4));
		vendor.setName1(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10, 36)));

		List<VendorCompany> companies = new ArrayList<VendorCompany>();

		VendorCompany vendorCompany = null;
		for (int j = 0; j < 0; j++) {
			vendorCompany = new VendorCompany();
			vendorCompany.setSourceSystem(vendor.getSourceSystem());
			vendorCompany.setSourceSystemId(vendor.getSourceSystemId());
			vendorCompany.setAccountNumber(vendor.getAccountNumber());
			vendorCompany.setCompanyCode(RandomStringUtils.randomNumeric(4));
			vendorCompany.setPostingBlock("");
			vendorCompany.setCreatedDate(new Date());
			vendorCompany.setPersonnelNumber(RandomStringUtils.randomNumeric(8));
			companies.add(vendorCompany);
		}
		vendor.setCompanies(companies);

		return vendor;

	}

}
