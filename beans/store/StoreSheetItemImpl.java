package excel.beans.store;

public abstract class StoreSheetItemImpl implements StoreSheetItem {
	
	protected String[] details;
	
	public void addDetails(int detailId, String detail, boolean onlyIfEmpty) {
		if (onlyIfEmpty) {
			if (this.details[detailId] == null || this.details[detailId].trim().length() == 0) {
				this.details[detailId] = detail;
			}
		} else {
			this.details[detailId] = detail;
		}
	}

	public String getDetails(int detailId) {
		return this.details[detailId];
	}

	public String toString() {
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < details.length; i++) {
			ret.append(details[i]).append("\t");
		}
		return ret.toString();
	}
}
