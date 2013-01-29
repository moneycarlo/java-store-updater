package excel.beans.store;

public interface StoreSheetItem {

	public void addDetails(int detailId, String detail);
	
	public String getDetails(int detailId);
	
	public String[] getHeaders();
	
	public int getTotalColumns();
	
	public String getSheetName();
	
	public int getSheetNumber();
}
