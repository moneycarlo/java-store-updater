package excel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import excel.beans.feed.ProductFeed;
import excel.beans.store.Attributes;
import excel.beans.store.Categories;
import excel.beans.store.Discounts;
import excel.beans.store.Options;
import excel.beans.store.Products;
import excel.beans.store.Rewards;
import excel.beans.store.Specials;
import excel.beans.store.Store;
import excel.beans.store.StoreSheetItem;

public class UpdateStore {
	
	private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ConfigFileReader.initialize();
		
		String feedFile = ConfigFileReader.getValue("feed.file.input");
		String exportedFile = ConfigFileReader.getValue("export.file.input");
		String updatedFile = ConfigFileReader.getValue("modified.import.file.output");
		String discontinuedFile = ConfigFileReader.getValue("discontinued.file.input");
		
		boolean errors = false;
		
		if (feedFile == null || !(new File(feedFile)).exists()) {
			System.out.println("Input feed file missing or does not exist at location specified\nCheck feed.file.input in properties file\n");
			errors = true;
		}

		if (exportedFile == null || !(new File(exportedFile)).exists()) {
			System.out.println("Exported store file missing or does not exist at location specified\nCheck export.file.input in properties file\n");
			errors = true;
		}
		
		if (discontinuedFile == null || !(new File(discontinuedFile)).exists()) {
			System.out.println("Discontinued file location missing or file does not exist at location spefified\nCheck discontinued.file.input in properties file\n");
			errors = true;
		}
		
		if (updatedFile == null) {
			System.out.println("Cannot write updated file at location specified\nCheck modified.import.file.output in properties file\n");
			errors = true;
		}
		
		if (errors) {
			System.out.println("Cannot contiue, please correct the errors above and try again\n");
			System.exit(-1);
		}
		
		UpdateStore create = new UpdateStore();

		// read the feed file
		Map<String, ProductFeed> feedContents = create.readFeed(feedFile);

		// read the last exported contents
		Store st = create.readExportedContents(exportedFile);
		
		// Read list of disabled SKUs
		List<String> discontinuedProducts = create.readDiscontinuedProducts(discontinuedFile);
		
		// update categories
		create.updateCategories(st, feedContents);
		
		// update products
		create.updateProducts(st, feedContents, discontinuedProducts);
		
		// recreate all attributes
		create.updateAttributes(st, feedContents);
		
		// write excel file back
		create.writeStoreContents(updatedFile, st);

		System.out.println("Finished");

	}
	
	/**
	 * Generate contents for Attributes sheet
	 * @param storeContents
	 * @param feedContents
	 */
	private void updateAttributes(Store storeContents, Map<String, ProductFeed> feedContents) {
		
		List<StoreSheetItem> storeProducts = storeContents.getAllProducts();
		Map<String, StoreSheetItem> productMap = generateItemMap(storeProducts, Products.SKU);
		
		List<ProductFeed> feedList = getFeedList(feedContents);
		
		List<StoreSheetItem> attributesliList = new ArrayList<StoreSheetItem>();
		ProductFeed prod;
		StoreSheetItem storeAttribute;
		StoreSheetItem storeProduct;
		
		String sku;
		String productId;
		
		// go through all the the products in the feed file
		for (int i = 0; i < feedList.size(); i++) {
			
			// get the product
			prod = feedList.get(i);
			
			// product sku
			sku = prod.getProductDetails(ProductFeed.PRODUCT_CODE);
			
			// get the product in the store based on the sku
			storeProduct =  productMap.get(sku);
			
			// if the product is missing, we cannot get the product id to put in attributes, ignore the product
			// should not ideally happen since the product is already updated, but still a safe check to avoid NPE
			if (storeProduct == null) {
				System.err.println("Product with SKU " + sku + " not found");
				continue;
			}

			// get the product id in DB
			productId = storeProduct.getDetails(Products.PRODUCT_ID);
			
			// if color exist, add that
			if (!isEmpty(prod.getProductDetails(ProductFeed.COLOR))) {
				storeAttribute = createAttributes(productId, "1", "Color", "Color", prod.getProductDetails(ProductFeed.COLOR), "1");
				attributesliList.add(storeAttribute);
			}
			
			// if scent exist, add that
			if (!isEmpty(prod.getProductDetails(ProductFeed.SCENT))) {
				storeAttribute = createAttributes(productId, "1", "Scent", "Scent", prod.getProductDetails(ProductFeed.SCENT), "2");
				attributesliList.add(storeAttribute);
			}
			
			// if ingredients units exist, add that
			if (!isEmpty(prod.getProductDetails(ProductFeed.INGREDIENTS))) {
				storeAttribute = createAttributes(productId, "1", "Ingredients", "Ingredients", prod.getProductDetails(ProductFeed.INGREDIENTS), "3");
				attributesliList.add(storeAttribute);
			}
			
			// if country exist, add that
			if (!isEmpty(prod.getProductDetails(ProductFeed.COUNTRY_OF_ORIGIN))) {
				storeAttribute = createAttributes(productId, "1", "Country of Origin", "Country of Origin", prod.getProductDetails(ProductFeed.COUNTRY_OF_ORIGIN), "4");
				attributesliList.add(storeAttribute);
			}

			// if selling units exist, add that
			if (!isEmpty(prod.getProductDetails(ProductFeed.SELLING_UNIT))) {
				storeAttribute = createAttributes(productId, "1", "Packaged", "Selling Unit", prod.getProductDetails(ProductFeed.SELLING_UNIT), "5");
				attributesliList.add(storeAttribute);
			}
			
		}
		
		// put all the attributes back in store
		storeContents.putAllAttributes(generateItemListMap(attributesliList, Attributes.PRODUCT_ID));
		
	}
	
	/**
	 * Util to check if a string is empty
	 * @param str
	 * @return
	 */
	private boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}
	
	/**
	 * Create attribute object
	 * @param productId product id value
	 * @param languageId language id value
	 * @param attGroup attribute group value
	 * @param attName attribute name value
	 * @param text text value
	 * @param sortOrder sort order value
	 * @return generated {@link Attributes} object
	 */
	private Attributes createAttributes(String productId, String languageId, String attGroup, String attName, String text, String sortOrder) {
		
		Attributes attribute = new Attributes();
		attribute.addDetails(Attributes.PRODUCT_ID, productId);
		attribute.addDetails(Attributes.LANGUAGE_ID, languageId);
		attribute.addDetails(Attributes.ATTRIBUTE_GROUP, attGroup);
		attribute.addDetails(Attributes.ATTRIBUTE_NAME, attName);
		attribute.addDetails(Attributes.TEXT, text);
		attribute.addDetails(Attributes.SORT_ORDER, sortOrder);
	
		return attribute;
	}
	
	
	
	/**
	 * Update the contents for Products sheet
	 * @param storeContents
	 * @param feedContents
	 * @param discontinuedProducts
	 */
	private void updateProducts(Store storeContents, Map<String, ProductFeed> feedContents, List<String> discontinuedProducts) {
		
		// get all the existing categories
		List<StoreSheetItem> storeCategories = storeContents.getAllCategories();
		// generate categories map with category name as key and entire category as value
		Map<String, StoreSheetItem> categoryMap = generateItemMap(storeCategories, Categories.NAME);
		
		// get all existing products
		List<StoreSheetItem> storeProducts = storeContents.getAllProducts();
		// generate products map with SKU as key and entire product as value
		Map<String, StoreSheetItem> productMap = generateItemMap(storeProducts, Products.SKU);
		
		// List to hold modified products
		List<StoreSheetItem> updatedStoreProducts = storeContents.getAllProducts();
		
		// list of feed items
		List<ProductFeed> feedList = getFeedList(feedContents);
		
		ProductFeed prod;
		StoreSheetItem storeProd;
		
		// get the product id
		int maxId = getMaxId(storeProducts, Products.PRODUCT_ID);
		
		String sku;
		String image;
		String seo;
		String status;
		String metadesc;
		String prodFeatures;
		
		for (int i = 0; i < feedList.size(); i++) {
			
			prod = feedList.get(i);
			sku = prod.getProductDetails(ProductFeed.PRODUCT_CODE);
			storeProd = productMap.get(sku);
			
			if (storeProd == null) {
				// create a new product with default information
				// these information need not be updated
				storeProd = new Products();
				storeProd.addDetails(Products.PRODUCT_ID, Integer.toString(++maxId));
				storeProd.addDetails(Products.REQUIRES_SHIPPING, "yes");
				storeProd.addDetails(Products.POINTS, "0");
				storeProd.addDetails(Products.DATE_ADDED, DATETIME_FORMAT.format(new Date()));
				storeProd.addDetails(Products.DATE_MODIFIED, DATETIME_FORMAT.format(new Date()));
				storeProd.addDetails(Products.DATE_AVAILABLE, DATE_FORMAT.format(new Date()));
				storeProd.addDetails(Products.UNIT, "lb");
				storeProd.addDetails(Products.LENGTH_UNIT, "in");
				storeProd.addDetails(Products.TAX_CLASS_ID, "9");
				storeProd.addDetails(Products.VIEWED, "5");
				storeProd.addDetails(Products.LANGUAGE_ID, "1");
				storeProd.addDetails(Products.STOCK_STATUS_ID, "5");
				storeProd.addDetails(Products.STORE_IDS, "0");
				storeProd.addDetails(Products.SUBTRACT, "true");
				storeProd.addDetails(Products.MINIMUM, "1");
				updatedStoreProducts.add(storeProd);
			}
			
			// update product information from feed
			// currently the image field is incorrect from feed and prod_code is used
			// image = prod.getProductDetails(ProductFeed.IMAGE);
			// image = image.substring(image.lastIndexOf("/")); - used when image field is correct

			metadesc = prod.getProductDetails(ProductFeed.SHORT_DESCRIPTION);
			metadesc = metadesc.substring(0,metadesc.indexOf("."));
			storeProd.addDetails(Products.META_DESCRIPTION, metadesc + ".");
			image = prod.getProductDetails(ProductFeed.PRODUCT_CODE).trim().replaceAll(" ", "-");			
			storeProd.addDetails(Products.IMAGE_NAME, "data/" + image + ".jpg");
			storeProd.addDetails(Products.NAME, prod.getProductDetails(ProductFeed.PRODUCT_NAME));
			// comma separated categories
			storeProd.addDetails(Products.CATEGORIES,
					lookupCategories(prod.getProductDetails(ProductFeed.CATEGORY),
							prod.getProductDetails(ProductFeed.SUB_CATEGORY_1),
							prod.getProductDetails(ProductFeed.SUB_CATEGORY_2),
							categoryMap));
			storeProd.addDetails(Products.SKU, prod.getProductDetails(ProductFeed.PRODUCT_CODE));
			storeProd.addDetails(Products.UPC, prod.getProductDetails(ProductFeed.UPC_CODE));
			storeProd.addDetails(Products.QUANTITY, prod.getProductDetails(ProductFeed.QTY_ON_HAND));
			storeProd.addDetails(Products.MODEL, prod.getProductDetails(ProductFeed.PRODUCT_CODE));
			storeProd.addDetails(Products.MANUFACTURER, prod.getProductDetails(ProductFeed.BRAND));
			storeProd.addDetails(Products.PRICE, prod.getProductDetails(ProductFeed.MSRP));
			storeProd.addDetails(Products.WEIGHT, checkAndReturnDefault(prod.getProductDetails(ProductFeed.PRODUCT_WEIGHT_LBS), "0"));
			storeProd.addDetails(Products.LENGTH, checkAndReturnDefault(prod.getProductDetails(ProductFeed.LENGTH_INCHES), "0"));
			storeProd.addDetails(Products.WIDTH, checkAndReturnDefault(prod.getProductDetails(ProductFeed.WIDTH_INCHES), "0"));
			storeProd.addDetails(Products.HEIGHT, checkAndReturnDefault(prod.getProductDetails(ProductFeed.HEIGHT_INCHES), "0"));
			status = prod.getProductDetails(ProductFeed.STATUS).trim().replaceAll("N/A#","false");
			storeProd.addDetails(Products.STATUS_ENABLED, "Available".equalsIgnoreCase(status) ? "true" : "false");
			seo = prod.getProductDetails(ProductFeed.PRODUCT_NAME).trim()
																.replaceAll("'", "-").replaceAll("\"", "-")
																.replaceAll(" ", "-").replaceAll("&", "-")
																.replaceAll(":", "-").replaceAll("/", "-")
																.replaceAll("\\+", "-").replaceAll("%", "-")
																.replaceAll(",", "-")
																.replaceAll("[-]+", "-"); // replace multiple dash with single dash
			storeProd.addDetails(Products.SEO_KEYWORD, seo);
			// break separated short description and product features
			prodFeatures = prod.getProductDetails(ProductFeed.PRODUCT_FEATURES);
			if (isEmpty(prodFeatures)) {
				prodFeatures = "";
			} else {
				if (!prodFeatures.startsWith("<li>")) {
					prodFeatures = "<li>" + prodFeatures;
				}
				prodFeatures = "<br><br><b>Product Features</b><br>" + prodFeatures;
			}
			storeProd.addDetails(Products.DESCRIPTION, prod.getProductDetails(ProductFeed.SHORT_DESCRIPTION) + prodFeatures);
			
		}
		
		// loop through the discontinued products and set quantity as 0
		for (int i = 0; i < discontinuedProducts.size(); i++) {
			
			sku = discontinuedProducts.get(i);
			storeProd = productMap.get(sku);
			
			if (storeProd != null) { // if the product does not exist in store, do nothing
				storeProd.addDetails(Products.QUANTITY, "0");
			}
			
		}
		
		// put all the updated products back in Store
		storeContents.putAllProducts(updatedStoreProducts);
		
	}
	
	/**
	 * Get comma separated category ids
	 * @param category Category
	 * @param subcat1 Sub category 1
	 * @param subcat2 Sub Category 2
	 * @param categoryMap Category map
	 * @return
	 */
	private String lookupCategories(String category, String subcat1, String subcat2, Map<String, StoreSheetItem> categoryMap) {

		StringBuffer catBuffer = new StringBuffer();
		StoreSheetItem cat;
		
		boolean added = false;
		
		cat = categoryMap.get(category);
		if (cat != null) {
			catBuffer.append(cat.getDetails(Categories.CATEGORY_ID));
			added = true;
		}
		
		cat = categoryMap.get(subcat1);
		if (cat != null) {
			if (added) {
				catBuffer.append(",");
			}
			catBuffer.append(cat.getDetails(Categories.CATEGORY_ID));
			added = true;
		}
		
		cat = categoryMap.get(subcat2);
		if (cat != null) {
			if (added) {
				catBuffer.append(",");
			}
			catBuffer.append(cat.getDetails(Categories.CATEGORY_ID));
		}
		
		return catBuffer.toString();
	}

	/**
	 * Update the contents for Categories sheet
	 * @param storeCategories Existing categories in store
	 * @param feedContents Feed contents
	 * @return updated categories
	 */
	private void updateCategories(Store storeContents, Map<String, ProductFeed> feedContents) {
		
		// all existing categories in store
		List<StoreSheetItem> storeCategories = storeContents.getAllCategories();
		
		// Map to hold modified categories
		Map<String, StoreSheetItem> updatedStoreCategories = new HashMap<String, StoreSheetItem>();

		// list of feed items
		List<ProductFeed> feedList = getFeedList(feedContents);
		
		// map of categories with name as key and entire category as value
		Map<String, StoreSheetItem> categoryMap = generateItemMap(storeCategories, Categories.NAME);
		
		// get max id of existing categories
		int maxId = getMaxId(storeCategories, Categories.CATEGORY_ID);
		
		StoreSheetItem category;
		
		String cat;
		String subCat1;
		String subCat2;
		
		// run through all the category
		for (int i = 0; i < feedList.size(); i++) {
			
			// get the category from the feed
			cat = feedList.get(i).getProductDetails(ProductFeed.CATEGORY);
			
			// get existing category from store
			category = categoryMap.get(cat);
			
			// if category does not exist, create a new category
			if (category == null) {
				category = populateCategories(Integer.toString(++maxId), cat, "false", "0", "1", "1", "0", "true");
				category.addDetails(Categories.PARENT_ID, "0");
				categoryMap.put(cat, category);
			}
			// add the category to the list, there is nothing to be done if the category already exist
			updatedStoreCategories.put(cat, category);
		}
		
		// run through all the sub category 1
		for (int i = 0; i < feedList.size(); i++) {
			
			// get the category from the feed
			cat = feedList.get(i).getProductDetails(ProductFeed.CATEGORY);
			
			// get the sub category 1 from the feed
			subCat1 = feedList.get(i).getProductDetails(ProductFeed.SUB_CATEGORY_1);
			
			// get existing category from store based on sub category 1 value
			category = categoryMap.get(subCat1);
			
			// if sub category 1 does not exist, create a new category with sub category 1 value
			if (category == null) {
				categoryMap.put(subCat1, populateCategories(Integer.toString(++maxId), subCat1, "false", "0", "1", "1", "0", "true"));
			}
			// set the parent id
			category.addDetails(Categories.PARENT_ID, categoryMap.get(cat).getDetails(Categories.CATEGORY_ID));
			// add the category to the list
			updatedStoreCategories.put(subCat1, category);
		}
		
		// run through all the sub category 2
		for (int i = 0; i < feedList.size(); i++) {
			
			// get the sub category 1 from feed
			subCat1 = feedList.get(i).getProductDetails(ProductFeed.SUB_CATEGORY_1);
			
			// get the sub category 2 from feed
			subCat2 = feedList.get(i).getProductDetails(ProductFeed.SUB_CATEGORY_2);
			
			// get the existing category from store based on sub category 2 value
			category = categoryMap.get(subCat2);
			
			// if sub category 2 does not exist, create a new category with sub category 2 value
			if (category == null) {
				categoryMap.put(subCat2, populateCategories(Integer.toString(++maxId), subCat2, "false", "0", "1", "1", "0", "true"));
			}

			// set the parent id
			category.addDetails(Categories.PARENT_ID, categoryMap.get(subCat1).getDetails(Categories.CATEGORY_ID));
			// add the category to the list
			updatedStoreCategories.put(subCat2, category);
		}
		
		// put all categories back in store
		storeContents.putAllCategories(generateItemList2(updatedStoreCategories));
		
	}
	
	/**
	 * Generate a category object
	 * @param id category id
	 * @param name category name
	 * @param top top value
	 * @param column column value
	 * @param sortOrder sort order value
	 * @param languageId language id value
	 * @param storeId store id value
	 * @param statusEnabled enabled value
	 * @return generated {@link Categories} object
	 */
	private Categories populateCategories(String id, String name, String top, String column, String sortOrder, String languageId, String storeId, String statusEnabled) {
		
		Categories category = new Categories();
		category.addDetails(Categories.CATEGORY_ID, id);
		category.addDetails(Categories.NAME, name.replaceAll("&", "and"));
		category.addDetails(Categories.TOP, top);
		category.addDetails(Categories.COLUMNS, column);
		category.addDetails(Categories.SORT_ORDER, sortOrder);
		category.addDetails(Categories.DATE_ADDED, DATETIME_FORMAT.format(new Date()));
		category.addDetails(Categories.DATE_MODIFIED, DATETIME_FORMAT.format(new Date()));
		category.addDetails(Categories.LANGUAGE_ID, languageId);
		category.addDetails(Categories.SEO_KEYWORD, name);
		category.addDetails(Categories.META_DESCRIPTION, name);
		category.addDetails(Categories.META_KEYWORDS, name);
		category.addDetails(Categories.STORE_IDS, storeId);
		category.addDetails(Categories.STATUS_ENABLED, statusEnabled);
		
		return category;
	}
	
	/**
	 * Get the max id from a list of StoreSheetItems
	 * @param storeItems {@link List} if {@link StoreSheetItem} instance
	 * @param idIndex index if the id
	 * @return max id
	 */
	private int getMaxId(List<StoreSheetItem> storeItems, int idIndex) {
		int maxId = 0;
		int id;
		
		try {
			for (int i = 0; i < storeItems.size(); i++) {
				id = Integer.parseInt(storeItems.get(i).getDetails(idIndex));
				maxId = maxId > id ? maxId : id;
			}
		} catch (NumberFormatException ex) {}
		
		return maxId;
	}

	/**
	 * Given a map with key value pair, this method returns a list of all values
	 * @param feedContents map
	 * @return list of values
	 */
	private List<ProductFeed> getFeedList(Map<String, ProductFeed> feedContents) {
		
		List<ProductFeed> itemsList = new ArrayList<ProductFeed>();

		Iterator<String> productIter = feedContents.keySet().iterator();

		while (productIter.hasNext()) {
			itemsList.add(feedContents.get(productIter.next()));
		}

		return itemsList;
	}

	/**
	 * Writes the new file with updated contents
	 * @param outStoreFile output file name
	 * @param storeContents updated contents to write
	 */
	private void writeStoreContents(String outStoreFile, Store storeContents) {

		File file = new File(outStoreFile);

		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = null;

		try {

			// create a new workbook
			workbook = Workbook.createWorkbook(file, wbSettings);

			// add categories
			addSheet(workbook, Categories.class, storeContents.getAllCategories());

			// add products
			addSheet(workbook, Products.class, storeContents.getAllProducts());

			// add options
			addSheet(workbook, Options.class, generateItemList(storeContents.getAllOptions()));

			// add attributes
			addSheet(workbook, Attributes.class, generateItemList(storeContents.getAllAttributes()));

			// add specials
			addSheet(workbook, Specials.class, generateItemList(storeContents.getAllSpecials()));

			// add discount
			addSheet(workbook, Discounts.class, generateItemList(storeContents.getAllDiscounts()));

			// add rewards
			addSheet(workbook, Rewards.class, generateItemList(storeContents.getAllRewards()));

			// write all the contents
			workbook.write();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (WriteException e) {
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Add a new sheet in the workbook and write the contents for that sheet
	 * @param workbook workbook instance
	 * @param sheetClass The type of sheet being written
	 * @param sheet contents
	 * @throws RowsExceededException
	 * @throws {@link WriteException}
	 * @throws InstantiationException
	 * @throws {@link IllegalAccessException}
	 */
	private void addSheet(WritableWorkbook workbook, Class<? extends StoreSheetItem> sheetClass, List<StoreSheetItem> sheet) throws RowsExceededException, WriteException, InstantiationException, IllegalAccessException {

		StoreSheetItem item = (StoreSheetItem)sheetClass.newInstance();

		System.out.println("Writing " + item.getSheetName() + " at index " + item.getSheetNumber());

		workbook.createSheet(item.getSheetName(), item.getSheetNumber());
		WritableSheet excelSheet = workbook.getSheet(item.getSheetNumber());
		addHeader(excelSheet, item.getHeaders());

		for (int i = 0; i < sheet.size(); i++) {

			for (int j = 0; j < sheet.get(0).getTotalColumns(); j++) {
				addCell(excelSheet, j, i+1, sheet.get(i).getDetails(j));
			}

		}
	}

	/**
	 * Add header to the sheet
	 * @param sheet sheet instance
	 * @param headers header
	 * @throws RowsExceededException
	 * @throws {@link WriteException}
	 */
	private void addHeader(WritableSheet sheet, String[] headers) throws RowsExceededException, WriteException {
		for (int i = 0; i < headers.length; i++) {
			addCell(sheet, i, 0, headers[i]);
		}
	}

	/**
	 * Add a cell in the sheet at given position
	 * @param sheet sheet instance
	 * @param column cell column
	 * @param row cell row
	 * @param contents contents to write
	 * @throws WriteException
	 * @throws {@link RowsExceededException}
	 */
	private void addCell(WritableSheet sheet, int column, int row, String contents) throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, contents);
		sheet.addCell(label);
	}
	
	/**
	 * Reads the disabled product sheet and returns the SKUs of disabled products in a {@link List}
	 * @param disabledFileLocation file location of disabled products, file format must be csv
	 * @return {@link List} of disabled SKUs
	 */
	private List<String> readDiscontinuedProducts(String disabledFileLocation) {
		
		List<String> disabledSKUs = new ArrayList<String>();
		
		BufferedReader reader = null;
		String singleLine;
		String[] lineContents;
		
		try {
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(disabledFileLocation)));
			
			while ((singleLine = reader.readLine()) != null) {
				lineContents = singleLine.split(",");
				if (lineContents.length > 0) {
					disabledSKUs.add(lineContents[0]);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally  {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return disabledSKUs;
	}

	/**
	 * Reads the contents from exported excel sheet and returns all the contents
	 * @param inStoreFile file location
	 * @return contents from all the sheets
	 */
	private Store readExportedContents(String inStoreFile) {
		Store st = new Store();

		File inputWorkbook = new File(inStoreFile);
		Workbook w = null;
		List<StoreSheetItem> items;

		try {
			w = Workbook.getWorkbook(inputWorkbook);

			try {
				// categories
				items = readSheet(w, Categories.SHEET_INDEX);
				st.putAllCategories(items);

				// products
				items = readSheet(w, Products.SHEET_INDEX);
				st.putAllProducts(items);

				// options
				items = readSheet(w, Options.SHEET_INDEX);
				st.putAllOptions(generateItemListMap(items, Options.PRODUCT_ID));

				// attributes
				items = readSheet(w, Attributes.SHEET_INDEX);
				st.putAllAttributes(generateItemListMap(items, Attributes.PRODUCT_ID));

				// specials
				items = readSheet(w, Specials.SHEET_INDEX);
				st.putAllSpecials(generateItemListMap(items, Specials.PRODUCT_ID));

				// discounts
				items = readSheet(w, Discounts.SHEET_INDEX);
				st.putAllDiscounts(generateItemListMap(items, Discounts.PRODUCT_ID));

				// rewards
				items = readSheet(w, Rewards.SHEET_INDEX);
				st.putAllRewards(generateItemListMap(items, Rewards.PRODUCT_ID));

			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}


		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (w != null) {
				w.close();
			}
		}

		return st;
	}

	/**
	 * Given a list of {@link StoreSheetItem}, this method puts it in a map with the key values from the index passed
	 * @param items {@link List} of items
	 * @param keys index of values which should be used as key 
	 * @return
	 */
	private Map<String, List<StoreSheetItem>> generateItemListMap(List<StoreSheetItem> items, int... keys) {
		Map<String, List<StoreSheetItem>> itemMap = new HashMap<String, List<StoreSheetItem>>();
		StoreSheetItem item;
		List<StoreSheetItem> mapItems;
		
		StringBuffer keyBuffer = new StringBuffer();

		String key;

		for (int i = 0; i < items.size(); i++) {
			
			item = items.get(i);
			
			for (int j = 0; j < keys.length; j++) {
				keyBuffer.append(item.getDetails(keys[j])).append(";");
			}
			
			key = keyBuffer.toString();
			mapItems = itemMap.get(key);
			
			if (mapItems == null) {
				mapItems = new ArrayList<StoreSheetItem>();
				itemMap.put(key, mapItems);
			}
			
			mapItems.add(item);
		}

		return itemMap;
	}

	/**
	 * Puts items in a map against a key 
	 * @param items
	 * @param key
	 * @return
	 */
	private Map<String, StoreSheetItem> generateItemMap(List<StoreSheetItem> items, int key) {
		Map<String, StoreSheetItem> itemMap = new HashMap<String, StoreSheetItem>();
		StoreSheetItem item;
		
		for (int i = 0; i < items.size(); i++) {
			item = items.get(i);
			itemMap.put(item.getDetails(key), item);
		}
		
		return itemMap;
	}
	
	/**
	 * Given a Map with key value pair, where values are another list, this method puts all values in a list
	 * @param itemMap Map with key value pair, values are another list
	 * @return list of all values
	 */
	private List<StoreSheetItem> generateItemList(Map<String, List<StoreSheetItem>> itemMap) {
		List<StoreSheetItem> itemsList = new ArrayList<StoreSheetItem>();

		Iterator<String> productIter = itemMap.keySet().iterator();

		while (productIter.hasNext()) {
			itemsList.addAll(itemMap.get(productIter.next()));
		}

		return itemsList;
	}

	/**
	 * Given a Map with key value pair, this method puts all values in a list
	 * @param itemMap Map with key value pair
	 * @return list of all values
	 */
	private List<StoreSheetItem> generateItemList2(Map<String, StoreSheetItem> itemMap) {
		List<StoreSheetItem> itemsList = new ArrayList<StoreSheetItem>();
		
		Iterator<String> productIter = itemMap.keySet().iterator();
		
		while (productIter.hasNext()) {
			itemsList.add(itemMap.get(productIter.next()));
		}
		
		return itemsList;
	}
	
	/**
	 * Reads a numbered sheet in the workbook, used to read sheet exported from store
	 * @param workbook workbook instance
	 * @param sheetNumber sheet number to read
	 * @throws InstantiationException
	 * @throws {@link IllegalAccessException}
	 * @throws ClassNotFoundException
	 */
	private List<StoreSheetItem> readSheet(Workbook workbook, int sheetNumber) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<StoreSheetItem> sheetContents = new ArrayList<StoreSheetItem>();

		// Read Products
		Sheet sheet = workbook.getSheet(sheetNumber);
		String sheetName = sheet.getName();
		System.out.println("Reading sheet " + sheetName);

		StoreSheetItem sheetItem;
		Cell cell;

		// skip the header
		for (int i = 1; i < sheet.getRows(); i++) {

			sheetItem = (StoreSheetItem) Class.forName("excel.beans.store." + sheetName).newInstance();

			for (int j = 0; j < sheet.getColumns(); j++) {

				cell = sheet.getCell(j, i);

				sheetItem.addDetails(j, cell.getContents());

			}

			sheetContents.add(sheetItem);
		}

		return sheetContents;
	}



	/**
	 * Reads the input feed file
	 * @param feedFile feed file path
	 * @return {@link Map} with SKU as key and {@link ProductFeed} as value
	 * @throws IOException
	 */
	private Map<String, ProductFeed> readFeed(String feedFile) {
		File inputWorkbook = new File(feedFile);
		Workbook w;
		Map<String, ProductFeed> productFeed = new HashMap<String, ProductFeed>();
		ProductFeed singleProduct;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			System.out.println("Reading sheet " + sheet.getName());

			// skip the header
			for (int i = 1; i < sheet.getRows(); i++) {

				singleProduct = new ProductFeed();

				for (int j = 0; j < sheet.getColumns(); j++) {

					Cell cell = sheet.getCell(j, i);

					singleProduct.addProductDetails(j, cell.getContents());

				}

				if (productFeed.containsKey(singleProduct.getProductDetails(ProductFeed.PRODUCT_CODE))) {
					System.err.println(singleProduct.getProductDetails(ProductFeed.PRODUCT_CODE) + " is repeating");
				} else {
					productFeed.put(singleProduct.getProductDetails(ProductFeed.PRODUCT_CODE), singleProduct);
				}

			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return productFeed;
	}
	
	/**
	 * Checks if a value is missing (<code>null</code> or blank) and if yes, then return the default value
	 * @param value value to check
	 * @param defaultValue default value
	 * @return
	 */
	private String checkAndReturnDefault(String value, String defaultValue) {
		return (value == null || value.trim().length() == 0) ? defaultValue : value;
	}
	
	private static class ConfigFileReader {
		
		
		private static Properties props = null;
		private static boolean isInitialized = false;
		
		private static void initialize() {
			props = new Properties();
			try {
				props.load(new FileInputStream("C:\\aahmad755\\excel\\excel\\storeupdater.properties"));
				isInitialized = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private static String getValue(String key) {
			
			if (!isInitialized) {
				return null;
			} else {
				return props.getProperty(key);
			}
			
		}
		
	}

}
