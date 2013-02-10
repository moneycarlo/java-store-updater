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
	public static final int SUBCATEGORY = 1;
	public static final int SUBCATEGORY_2 = 2;
	public static final int BRAND = 3;
	public static final int OLD_PROD__NO = 4;
	public static final int NEW_UNFI_PROD_NO = 5;
	public static final int DATE_UPDATED = 6;
	public static final int STATUS = 7;
	public static final int QTY_ON_HAND = 8;
	public static final int PRODUCT_NAME = 9;
	public static final int SELLING_UNIT = 10;
	public static final int E_TAILER_PRICE = 11;
	public static final int SRP = 12;
	public static final int E_TAILER_MARGIN = 13;
	public static final int RETAILER_PRICE = 14;
	public static final int RETAILER_MARGIN = 15;
	public static final int MAP = 16;
	public static final int SHORT_DESCRIPTION = 17;
	public static final int PRODUCT_FEATURES = 18;
	public static final int INGREDIENTS = 19;
	public static final int COLOR = 20;
	public static final int SCENT = 21;
	public static final int UPC = 22;
	public static final int LENGTH_INCHES = 23;
	public static final int WIDTH_INCHES = 24;
	public static final int HEIGHT_INCHES = 25;
	public static final int WEIGHT_LBS = 26;
	public static final int COUNTRY_OF_ORIGIN = 27;
	public static final int IMAGE_URL = 28;

	public static final int TOTAL_COLUMNS = 29;

}

