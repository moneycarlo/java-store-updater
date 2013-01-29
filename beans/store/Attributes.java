package excel.beans.store;

public class Attributes extends StoreSheetItemImpl {
	
	public Attributes() {
		super.details = new String[TOTAL_COLUMNS];
		for (int i = 0; i < super.details.length; i++) {
			super.details[i] = "";
		}
	}
	
	public String[] getHeaders() {
		return HEADERS;
	}

	public String getSheetName() {
		return SHEET_NAME;
	}

	public int getSheetNumber() {
		return SHEET_INDEX;
	}

	public int getTotalColumns() {
		return TOTAL_COLUMNS;
	}

	public static final int PRODUCT_ID = 0;
	public static final int LANGUAGE_ID = 1;
	public static final int ATTRIBUTE_GROUP = 2;
	public static final int ATTRIBUTE_NAME = 3;
	public static final int TEXT = 4;
	public static final int SORT_ORDER = 5;

	private static final int TOTAL_COLUMNS = 6;
	
	private static final String[] HEADERS = new String[] {"product_id", "language_id", "attribute_group", "attribute_name", "text", "sort_order"};
	
	public static final String SHEET_NAME = "Attributes";

	public static final int SHEET_INDEX = 3;
}
