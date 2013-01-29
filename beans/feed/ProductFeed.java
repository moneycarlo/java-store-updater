package excel.beans.feed;

/**
 * Product details, represented by array of strings
 * @author aahmad
 *
 */
public class ProductFeed {

	private String[] productDetails;

	public ProductFeed() {
		productDetails = new String[TOTAL_COLUMNS];
	}

	/**
	 * Add product detail
	 * @param detailId
	 * @param detail
	 */
	public void addProductDetails(int detailId, String detail) {
		productDetails[detailId] = detail;
	}

	/**
	 * Get product detail
	 * @param detailId
	 * @return
	 */
	public String getProductDetails(int detailId) {
		return productDetails[detailId];
	}

	public static final int CATEGORY = 0;
	public static final int SUB_CATEGORY_1 = 1;
	public static final int SUB_CATEGORY_2 = 2;
	public static final int BRAND = 3;
	public static final int PRODUCT_CODE = 4;
	public static final int DATE_UPDATED = 5;
	public static final int STATUS = 6;
	public static final int QTY_ON_HAND = 7;
	public static final int PRODUCT_NAME = 8;
	public static final int SELLING_UNIT = 9;
	public static final int COST_TO_ETAILERS = 10;
	public static final int MSRP = 11;
	public static final int MAP = 12;
	public static final int ETAIL_MARGIN = 13;
	public static final int SHORT_DESCRIPTION = 14;
	public static final int PRODUCT_FEATURES = 15;
	public static final int INGREDIENTS = 16;
	public static final int COLOR = 17;
	public static final int SCENT = 18;
	public static final int UPC_CODE = 19;
	public static final int INDIVIDUAL_PRODUCT_DIMENSIONS_INCHES = 20;
	public static final int LENGTH_INCHES = 21;
	public static final int WIDTH_INCHES = 22;
	public static final int HEIGHT_INCHES = 23;
	public static final int IMAGE = 24;
	public static final int PRODUCT_WEIGHT_LBS = 25;
	public static final int COUNTRY_OF_ORIGIN = 26;

	public static final int TOTAL_COLUMNS = 27;

}

