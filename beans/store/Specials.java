package excel.beans.store;

public class Specials extends StoreSheetItemImpl {
	
	public Specials() {
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
	public static final int PRIORITY = 2;
	public static final int PRICE = 3;
	public static final int DATE_START = 4;
	public static final int DATE_END = 5;

	private static final int TOTAL_COLUMNS = 6;
	
	private static final String[] HEADERS = new String[] {"product_id", "customer_group", "priority", "price", "date_start", "date_end"};
	
	public static final String SHEET_NAME = "Specials";

	public static final int SHEET_INDEX = 4;
}
