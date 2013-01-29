package excel.beans.store;

public class Products extends StoreSheetItemImpl {
	
	public Products() {
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
	public static final int NAME = 1;
	public static final int CATEGORIES = 2;
	public static final int SKU = 3;
	public static final int UPC = 4;
	public static final int LOCATION = 5;
	public static final int QUANTITY = 6;
	public static final int MODEL = 7;
	public static final int MANUFACTURER = 8;
	public static final int IMAGE_NAME = 9;
	public static final int REQUIRES_SHIPPING = 10;
	public static final int PRICE = 11;
	public static final int POINTS = 12;
	public static final int DATE_ADDED = 13;
	public static final int DATE_MODIFIED = 14;
	public static final int DATE_AVAILABLE = 15;
	public static final int WEIGHT = 16;
	public static final int UNIT = 17;
	public static final int LENGTH = 18;
	public static final int WIDTH = 19;
	public static final int HEIGHT = 20;
	public static final int LENGTH_UNIT = 21;
	public static final int STATUS_ENABLED = 22;
	public static final int TAX_CLASS_ID = 23;
	public static final int VIEWED = 24;
	public static final int LANGUAGE_ID = 25;
	public static final int SEO_KEYWORD = 26;
	public static final int DESCRIPTION = 27;
	public static final int META_DESCRIPTION = 28;
	public static final int META_KEYWORDS = 29;
	public static final int ADDITIONAL_IMAGE_NAMES = 30;
	public static final int STOCK_STATUS_ID = 31;
	public static final int STORE_IDS = 32;
	public static final int LAYOUT = 33;
	public static final int RELATED_IDS = 34;
	public static final int TAGS = 35;
	public static final int SORT_ORDER = 36;
	public static final int SUBTRACT = 37;
	public static final int MINIMUM = 38;

	private static final int TOTAL_COLUMNS = 39;
	
	private static final String[] HEADERS = new String[] {"product_id", "name", "categories", "sku", "upc", "location", "quantity", "model", "manufacturer", "image_name", "requires\nshipping", "price", "points", "date_added", "date_modified", "date_available", "weight", "unit", "length", "width", "height", "length\nunit", "status\nenabled", "tax_class_id", "viewed", "language_id", "seo_keyword", "description", "meta_description", "meta_keywords", "additional image names", "stock_status_id", "store_ids", "layout", "related_ids", "tags", "sort_order", "subtract", "minimum"};

	public static final String SHEET_NAME = "Products";

	public static final int SHEET_INDEX = 1;
}
