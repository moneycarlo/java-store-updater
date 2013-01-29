package excel.beans.store;

public class Rewards extends StoreSheetItemImpl {
	
	public Rewards() {
		super.details = new String[TOTAL_COLUMNS];
		for (int i = 0; i < super.details.length; i++) {
			super.details[i] = "";
		}
	}
	
	public String[] getHeaders() {
		return HEADERS;
	}
	
	public int getTotalColumns() {
		return TOTAL_COLUMNS;
	}
	
	public String getSheetName() {
		return SHEET_NAME;
	}
	
	public int getSheetNumber() {
		return SHEET_INDEX;
	}

	public static final int PRODUCT_ID = 0;
	public static final int CUSTOMER_GROUP = 1;
	public static final int POINTS = 2;

	private static final int TOTAL_COLUMNS = 3;
	
	private static final String[] HEADERS = new String[] {"product_id", "customer_group", "points"};

	public static final String SHEET_NAME = "Rewards";

	public static final int SHEET_INDEX = 6;
}
