package excel.beans.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Store {

	private List<StoreSheetItem> categories;

	private List<StoreSheetItem> products;

	private Map<String, List<StoreSheetItem>> options;

	private Map<String, List<StoreSheetItem>> attributes;

	private Map<String, List<StoreSheetItem>> specials;

	private Map<String, List<StoreSheetItem>> discounts;

	private Map<String, List<StoreSheetItem>> rewards;

	////////////////// CATEGORIES START /////////////////////
	public List<StoreSheetItem> getAllCategories() {
		return categories;
	}

	public void putAllCategories(List<StoreSheetItem> categories) {
		this.categories = categories;
	}

	public void addCategories(StoreSheetItem category) {
		this.categories.add(category);
	}

	public StoreSheetItem getCategory(int index) {
		StoreSheetItem cat = null;
		if (this.categories == null || this.categories.size() == 0 || this.categories.size() < index) {
			cat = null;
		} else {
			cat = this.categories.get(index);
		}
		return cat;
	}

	////////////////// CATEGORIES  END /////////////////////

	////////////////// PRODUCTS START /////////////////////

	public List<StoreSheetItem> getAllProducts() {
		return products;
	}

	public void putAllProducts(List<StoreSheetItem> products) {
		this.products = products;
	}

	public void addProduct(StoreSheetItem product) {
		this.products.add(product);
	}

	public StoreSheetItem getProduct(int index) {
		StoreSheetItem cat = null;
		if (this.products == null || this.products.size() == 0 || this.products.size() < index) {
			cat = null;
		} else {
			cat = this.products.get(index);
		}
		return cat;
	}

	////////////////// PRODUCTS END /////////////////////


	////////////////// OPTIONS START /////////////////////

	public void putAllOptions(Map<String, List<StoreSheetItem>> options) {
		this.options = options;
	}

	public Map<String, List<StoreSheetItem>> getAllOptions() {
		return this.options;
	}

	public void addOptions(String productId, List<StoreSheetItem> options) {
		List<StoreSheetItem> existingOptions = this.options.get(productId);

		if (existingOptions == null) {
			existingOptions = new ArrayList<StoreSheetItem>();
			this.options.put(productId, existingOptions);
		}
		existingOptions.addAll(options);

	}

	public void addOptions(String productId, StoreSheetItem options) {
		List<StoreSheetItem> existingOptions = this.options.get(productId);

		if (existingOptions == null) {
			existingOptions = new ArrayList<StoreSheetItem>();
			this.options.put(productId, existingOptions);
		}
		existingOptions.add(options);

	}

	public List<StoreSheetItem> getAllOptionsForProduct(int productId) {
		List<StoreSheetItem> optionsForProduct = this.options.get(productId);

		return optionsForProduct;
	}

	////////////////// OPTIONS END /////////////////////

	////////////////// ATTRIBUTES START /////////////////////

	public void putAllAttributes(Map<String, List<StoreSheetItem>> attributes) {
		this.attributes = attributes;
	}

	public Map<String, List<StoreSheetItem>> getAllAttributes() {
		return this.attributes;
	}

	public void addAttributes(String productId, List<StoreSheetItem> attributes) {
		List<StoreSheetItem> existingAttributes = this.attributes.get(productId);

		if (existingAttributes == null) {
			existingAttributes = new ArrayList<StoreSheetItem>();
			this.attributes.put(productId, existingAttributes);
		}
		existingAttributes.addAll(attributes);

	}

	public void addAttributes(String productId, StoreSheetItem attributes) {
		List<StoreSheetItem> existingAttributes = this.attributes.get(productId);

		if (existingAttributes == null) {
			existingAttributes = new ArrayList<StoreSheetItem>();
			this.attributes.put(productId, existingAttributes);
		}
		existingAttributes.add(attributes);

	}

	public List<StoreSheetItem> getAllAttributesForProduct(int productId) {
		List<StoreSheetItem> attributesForProduct = this.attributes.get(productId);

		return attributesForProduct;
	}

	////////////////// ATTRIBUTES END /////////////////////

	////////////////// SPECIALS START /////////////////////
	
	public void putAllSpecials(Map<String, List<StoreSheetItem>> specials) {
		this.specials = specials;
	}
	
	public Map<String, List<StoreSheetItem>> getAllSpecials() {
		return this.specials;
	}
	
	public void addSpecials(String productId, List<StoreSheetItem> specials) {
		List<StoreSheetItem> existingSpecials = this.specials.get(productId);
		
		if (existingSpecials == null) {
			existingSpecials = new ArrayList<StoreSheetItem>();
			this.specials.put(productId, existingSpecials);
		}
		existingSpecials.addAll(specials);
		
	}
	
	public void addSpecials(String productId, StoreSheetItem specials) {
		List<StoreSheetItem> existingSpecials = this.specials.get(productId);
		
		if (existingSpecials == null) {
			existingSpecials = new ArrayList<StoreSheetItem>();
			this.specials.put(productId, existingSpecials);
		}
		existingSpecials.add(specials);
		
	}
	
	public List<StoreSheetItem> getAllSpecialsForProduct(int productId) {
		List<StoreSheetItem> specialsForProduct = this.specials.get(productId);
		
		return specialsForProduct;
	}
	
	////////////////// SPECIALS END /////////////////////
	
	////////////////// DISCOUNTS START /////////////////////
	
	public void putAllDiscounts(Map<String, List<StoreSheetItem>> discounts) {
		this.discounts = discounts;
	}
	
	public Map<String, List<StoreSheetItem>> getAllDiscounts() {
		return this.discounts;
	}
	
	public void addDiscounts(String productId, List<StoreSheetItem> discounts) {
		List<StoreSheetItem> existingDiscounts = this.discounts.get(productId);
		
		if (existingDiscounts == null) {
			existingDiscounts = new ArrayList<StoreSheetItem>();
			this.discounts.put(productId, existingDiscounts);
		}
		existingDiscounts.addAll(discounts);
		
	}
	
	public void addDiscounts(String productId, StoreSheetItem discounts) {
		List<StoreSheetItem> existingDiscounts = this.discounts.get(productId);
		
		if (existingDiscounts == null) {
			existingDiscounts = new ArrayList<StoreSheetItem>();
			this.discounts.put(productId, existingDiscounts);
		}
		existingDiscounts.add(discounts);
		
	}
	
	public List<StoreSheetItem> getAllDiscountsForProduct(int productId) {
		List<StoreSheetItem> discountsForProduct = this.discounts.get(productId);
		
		return discountsForProduct;
	}
	
	////////////////// DISCOUNTS END /////////////////////
	
	////////////////// REWARDS START /////////////////////
	
	public void putAllRewards(Map<String, List<StoreSheetItem>> rewards) {
		this.rewards = rewards;
	}
	
	public Map<String, List<StoreSheetItem>> getAllRewards() {
		return this.rewards;
	}
	
	public void addRewards(String productId, List<StoreSheetItem> rewards) {
		List<StoreSheetItem> existingRewards = this.rewards.get(productId);
		
		if (existingRewards == null) {
			existingRewards = new ArrayList<StoreSheetItem>();
			this.rewards.put(productId, existingRewards);
		}
		existingRewards.addAll(rewards);
		
	}
	
	public void addRewards(String productId, StoreSheetItem rewards) {
		List<StoreSheetItem> existingRewards = this.rewards.get(productId);
		
		if (existingRewards == null) {
			existingRewards = new ArrayList<StoreSheetItem>();
			this.rewards.put(productId, existingRewards);
		}
		existingRewards.add(rewards);
		
	}
	
	public List<StoreSheetItem> getAllRewardsForProduct(int productId) {
		List<StoreSheetItem> rewardsForProduct = this.rewards.get(productId);
		
		return rewardsForProduct;
	}
	
	////////////////// REWARDS END /////////////////////
	

}
