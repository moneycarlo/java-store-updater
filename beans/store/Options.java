package excel.beans.store;

public class Options extends StoreSheetItemImpl {

	public Options() {
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
	public static final int LANGUAGE_ID = 1;
	public static final int OPTION = 2;
	public static final int TYPE = 3;
	public static final int VALUE = 4;
	public static final int IMAGE = 5;
	public static final int REQUIRED = 6;
	public static final int QUANTITY = 7;
	public static final int SUBTRACT = 8;
	public static final int PRICE = 9;
	public static final int PRICE_PREFIX = 10;
	public static final int POINTS = 11;
	public static final int POINTS_PREFIX = 12;
	public static final int WEIGHT = 13;
	public static final int WEIGHT_PREFIX = 14;
	public static final int SORT_ORDER = 15;

	private static final int TOTAL_COLUMNS = 16;

	private static final String[] HEADERS = new String[] {"product_id", "language_id", "option", "type", "value", "image",  "required", "quantity", "subtract", "price", "price\nprefix", "points", "points\nprefix", "weight", "weight\nprefix", "sort_order"};

	public static final String SHEET_NAME = "Options";

	public static final int SHEET_INDEX = 2;
}