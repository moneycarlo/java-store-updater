package excel.beans.feed;

/**
 * Product details, represented by array of strings
 * @author aahmad
 *
 */
public class QtyOnHandFeed {

	private String[] productDetails;

	public QtyOnHandFeed() {
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
	
	public static final int PRODUCTNUMBER = 0;
	public static final int BRAND = 1;
	public static final int DESCRIPTION = 2;
	public static final int SIZE = 3;
	public static final int UNITTYPE = 4;
	public static final int UPC = 5;
	public static final int ONHAND = 6;
	public static final int DELIVERY_DUE_DATE = 7;
	
	public static final int TOTAL_COLUMNS = 8;

}
