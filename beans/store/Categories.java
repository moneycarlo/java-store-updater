package excel.beans.store;

public class Categories extends StoreSheetItemImpl {
	
	public Categories() {
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

	public static final int CATEGORY_ID = 0;
	public static final int PARENT_ID = 1;
	public static final int NAME = 2;
	public static final int TOP = 3;
	public static final int COLUMNS = 4;
	public static final int SORT_ORDER = 5;
	public static final int IMAGE_NAME = 6;
	public static final int DATE_ADDED = 7;
	public static final int DATE_MODIFIED = 8;
	public static final int LANGUAGE_ID = 9;
	public static final int SEO_KEYWORD = 10;
	public static final int DESCRIPTION = 11;
	public static final int META_DESCRIPTION = 12;
	public static final int META_KEYWORDS = 13;
	public static final int STORE_IDS = 14;
	public static final int LAYOUT = 15;
	public static final int STATUS_ENABLED = 16;

	private static final int TOTAL_COLUMNS = 17;
	
	private static final String[] HEADERS = new String[] {"category_id", "parent_id", "name", "top", "columns", "sort_order", "image_name", "date_added", "date_modified", "language_id", "seo_keyword", "description", "meta_description", "meta_keywords", "store_ids", "layout", "status\nenabled"};
	
	public static final String SHEET_NAME = "Categories";

	public static final int SHEET_INDEX = 0;
	
}
