package util;

public class PrintMockData {

	public static void main(String[] args) {

		String jsonString = JsonUtil.toJsonString(MockDataUtil.getVendor(3));
		System.out.println(jsonString);

	}

}
