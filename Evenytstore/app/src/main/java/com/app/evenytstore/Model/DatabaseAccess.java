package com.app.evenytstore.Model;

/**
 * Created by Enrique on 14/07/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import EvenytServer.model.Address;
import EvenytServer.model.Brand;
import EvenytServer.model.BrandForm;
import EvenytServer.model.Category;
import EvenytServer.model.Customer;
import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;
import EvenytServer.model.Size;
import EvenytServer.model.Subcategory;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }


    public HashMap<String,Customer> getAllCustomers() throws ParseException {
        HashMap  hashMap = new HashMap<String,Customer>();
        Cursor cursor = database.rawQuery("SELECT * FROM Customer", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Customer c = new Customer();
            c.setIdCustomer(cursor.getString(0));
            c.setEmail(cursor.getString(1));
            c.setPhoneNumber(cursor.getString(2));
            c.setName(cursor.getString(3));
            c.setLastName(cursor.getString(4));
            c.setBirthday(cursor.getString(5));
            c.setDNI(cursor.getString(6));
            c.setRUC(cursor.getString(7));
            Address address = new Address();
            address.setAddressName(cursor.getString(8));
            address.setAddressNumber(cursor.getString(9));
            address.setLatitude(BigDecimal.valueOf(cursor.getDouble(10)));
            address.setLongitude(BigDecimal.valueOf(cursor.getDouble(11)));
            address.setCity(cursor.getString(12));
            address.setDistrict(cursor.getString(13));
            c.setAddress(address);
            //products.add(p);
            hashMap.put(c.getIdCustomer(),c);
            cursor.moveToNext();
        }
        return hashMap;
    }


    public HashMap<String,Brand> getAllBrands() throws ParseException {
        HashMap  hashMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Brand", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Brand b = new Brand();
            b.setCode(cursor.getString(0));
            b.setName(cursor.getString(1));
            b.setDescription(cursor.getString(2));
            //products.add(p);
            hashMap.put(b.getCode(), b);
            cursor.moveToNext();
        }
        return hashMap;
    }


    public HashMap<String,BrandForm> getAllBrandForms() throws ParseException {
        HashMap  hashMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM BrandForm", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BrandForm b = new BrandForm();
            b.setCode(cursor.getString(0));
            b.setName(cursor.getString(1));
            b.setDescription(cursor.getString(2));
            //products.add(p);
            hashMap.put(b.getCode(), b);
            cursor.moveToNext();
        }
        return hashMap;
    }


    public HashMap<String,Category> getAllCategories() throws ParseException {
        HashMap  hashMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Category", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category c = new Category();
            c.setCode(cursor.getString(0));
            c.setName(cursor.getString(1));
            c.setDescription(cursor.getString(2));
            //products.add(p);
            hashMap.put(c.getCode(), c);
            cursor.moveToNext();
        }
        return hashMap;
    }


    public HashMap<String,Size> getAllSizes() throws ParseException {
        HashMap  hashMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Size", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Size s = new Size();
            s.setCode(cursor.getString(0));
            s.setName(cursor.getString(1));
            s.setDescription(cursor.getString(2));
            //products.add(p);
            hashMap.put(s.getCode(), s);
            cursor.moveToNext();
        }
        return hashMap;
    }


    public HashMap<String,Subcategory> getAllSubcategories() throws ParseException {
        HashMap  hashMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Subcategory", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Subcategory s = new Subcategory();
            s.setCode(cursor.getString(0));
            s.setName(cursor.getString(1));
            s.setDescription(cursor.getString(2));
            //products.add(p);
            hashMap.put(s.getCode(), s);
            cursor.moveToNext();
        }
        return hashMap;
    }


    public HashMap<String,Product> getAllProducts() throws ParseException {
        HashMap  hashMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Product", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product p = new Product();
            p.setCode(cursor.getString(0));
            p.setName(cursor.getString(1));
            p.setDescription(cursor.getString(2));
            p.setShortDescription(cursor.getString(3));
            p.setImgSrc(cursor.getString(4));
            p.setDateLastUpdate(cursor.getString(5));
            p.setCategoryCode(cursor.getString(6));
            p.setSubCategoryCode(cursor.getString(7));
            p.setBrandCode(cursor.getString(8));
            p.setBrandFormCode(cursor.getString(9));
            hashMap.put(p.getCode(), p);
            cursor.moveToNext();
        }
        return hashMap;
    }


    public HashMap<String,List<ProductXSize>> getAllProductsXSizes() throws ParseException {
        HashMap  hashMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Product", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ProductXSize p = new ProductXSize();
            p.setProductCode(cursor.getString(0));
            p.setSizeCode(cursor.getString(1));
            p.setPrice(BigDecimal.valueOf(cursor.getDouble(2)));
            if(!hashMap.containsKey(p.getProductCode()))
                hashMap.put(p.getProductCode(), new ArrayList<ProductXSize>());
            ((ArrayList<ProductXSize>)hashMap.get(p.getProductCode())).add(p);

            cursor.moveToNext();
        }
        return hashMap;
    }


    public void insertCustomer(Customer c){
        ContentValues cv = new ContentValues();
        cv.put("Id" , c.getIdCustomer());
        cv.put("Email", c.getEmail());
        cv.put("Phone", c.getPhoneNumber());
        cv.put("Name", c.getName());
        cv.put("LastName", c.getLastName());
        cv.put("Birthday", c.getBirthday());
        cv.put("Dni", c.getDNI());
        cv.put("Ruc", c.getRUC());
        cv.put("AddressName", c.getAddress().getAddressName());
        cv.put("AddressNumber", c.getAddress().getAddressNumber());
        cv.put("AddressLatitude", c.getAddress().getLatitude().doubleValue());
        cv.put("AddressLongitude", c.getAddress().getLongitude().doubleValue());
        cv.put("City", c.getAddress().getCity());
        cv.put("District", c.getAddress().getDistrict());

        database.insert("Customer", null, cv);
        Shelf.getHashCustomers().put(c.getIdCustomer(), c);
    }


    public void updateCustomer(Customer c)
    {
        ContentValues cv = new ContentValues();
        cv.put("Email", c.getEmail());
        cv.put("Phone", c.getPhoneNumber());
        cv.put("Name", c.getName());
        cv.put("LastName", c.getLastName());
        cv.put("Birthday", c.getBirthday());
        cv.put("Dni", c.getDNI());
        cv.put("Ruc", c.getRUC());
        cv.put("AddressName", c.getAddress().getAddressName());
        cv.put("AddressNumber", c.getAddress().getAddressNumber());
        cv.put("AddressLatitude", c.getAddress().getLatitude().doubleValue());
        cv.put("AddressLongitude", c.getAddress().getLongitude().doubleValue());
        cv.put("City", c.getAddress().getCity());
        cv.put("District", c.getAddress().getDistrict());
        String[] whereArgs = new String[] {c.getIdCustomer()};
        database.update("Category", cv, "Id=?", whereArgs);
        Shelf.getHashCustomers().put(c.getIdCustomer(), c);
    }


    public void insertBrands(List<Brand> brands){
        for(Brand b : brands){
            ContentValues cv = new ContentValues();
            cv.put("Code" , b.getCode());
            cv.put("Name", b.getName());
            cv.put("Description", b.getDescription());
            database.insert("Brand", null, cv);
            Shelf.getHashBrands().put(b.getCode(), b);
        }
    }

    public void updateBrands(List<Brand> brands){
        for(Brand b : brands){
            ContentValues cv = new ContentValues();
            cv.put("Name", b.getName());
            cv.put("Description", b.getDescription());
            String[] whereArgs = new String[] {b.getCode()};
            database.update("Brand", cv, "Code=?", whereArgs);
            Shelf.getHashBrands().put(b.getCode(), b);
        }
    }

    public void deleteBrands(List<Brand> brands){
        for(Brand b : brands){
            String[] whereArgs = new String[] {b.getCode()};
            database.delete("Brand","Code=?", whereArgs);
            Shelf.getHashBrands().remove(b.getCode());
        }
    }


    public void insertProducts(List<Product> products){
        for(Product p : products){
            ContentValues cv = new ContentValues();
            cv.put("Code" , p.getCode());
            cv.put("Name", p.getName());
            cv.put("Description", p.getDescription());
            cv.put("ShortDescription", p.getShortDescription());
            cv.put("ImgSrc", p.getImgSrc());
            cv.put("DateLastUpdate", p.getDateLastUpdate());
            cv.put("CategoryCode", p.getCategoryCode());
            cv.put("SubcategoryCode", p.getSubCategoryCode());
            cv.put("BrandCode", p.getBrandCode());
            cv.put("BrandFormCode", p.getBrandFormCode());
            database.insert("Product", null, cv);
            Shelf.getHashProducts().put(p.getCode(), p);
        }
    }

    public void updateProducts(List<Product> products){
        for(Product p : products){
            ContentValues cv = new ContentValues();
            cv.put("Name", p.getName());
            cv.put("Description", p.getDescription());
            cv.put("ShortDescription", p.getShortDescription());
            cv.put("ImgSrc", p.getImgSrc());
            cv.put("DateLastUpdate", p.getDateLastUpdate());
            cv.put("CategoryCode", p.getCategoryCode());
            cv.put("SubcategoryCode", p.getSubCategoryCode());
            cv.put("BrandCode", p.getBrandCode());
            cv.put("BrandFormCode", p.getBrandFormCode());
            String[] whereArgs = new String[] {p.getCode()};
            database.update("Product", cv, "Code=?", whereArgs);
            Shelf.getHashProducts().put(p.getCode(), p);
        }
    }

    public void deleteProducts(List<Product> products){
        for(Product p : products){
            String[] whereArgs = new String[] {p.getCode()};
            database.delete("Product","Code=?", whereArgs);
            Shelf.getHashProducts().remove(p.getCode());
        }
    }


    public void insertBrandForms(List<BrandForm> brandForms){
        for(BrandForm b : brandForms){
            ContentValues cv = new ContentValues();
            cv.put("Code" , b.getCode());
            cv.put("Name", b.getName());
            cv.put("Description", b.getDescription());
            database.insert("BrandForm", null, cv);
            Shelf.getHashBrandForms().put(b.getCode(), b);
        }
    }

    public void updateBrandForms(List<BrandForm> brandForms){
        for(BrandForm b : brandForms){
            ContentValues cv = new ContentValues();
            cv.put("Name", b.getName());
            cv.put("Description", b.getDescription());
            String[] whereArgs = new String[] {b.getCode()};
            database.update("BrandForm", cv, "Code=?", whereArgs);
            Shelf.getHashBrandForms().put(b.getCode(), b);
        }
    }

    public void deleteBrandForms(List<BrandForm> brandForms){
        for(BrandForm b : brandForms){
            String[] whereArgs = new String[] {b.getCode()};
            database.delete("BrandForm","Code=?", whereArgs);
            Shelf.getHashBrandForms().remove(b.getCode());
        }
    }


    public void insertCategories(List<Category> categories){
        for(Category c : categories){
            ContentValues cv = new ContentValues();
            cv.put("Code" , c.getCode());
            cv.put("Name", c.getName());
            cv.put("Description", c.getDescription());
            database.insert("Category", null, cv);
            Shelf.getHashCategories().put(c.getCode(), c);
        }
    }

    public void updateCategories(List<Category> categories){
        for(Category c : categories){
            ContentValues cv = new ContentValues();
            cv.put("Name", c.getName());
            cv.put("Description", c.getDescription());
            String[] whereArgs = new String[] {c.getCode()};
            database.update("Category", cv, "Code=?", whereArgs);
            Shelf.getHashCategories().put(c.getCode(), c);
        }
    }

    public void deleteCategories(List<Category> categories){
        for(Category c : categories){
            String[] whereArgs = new String[] {c.getCode()};
            database.delete("Category","Code=?", whereArgs);
            Shelf.getHashCategories().remove(c.getCode());
        }
    }


    public void insertSizes(List<Size> sizes){
        for(Size s : sizes){
            ContentValues cv = new ContentValues();
            cv.put("Code" , s.getCode());
            cv.put("Name", s.getName());
            cv.put("Description", s.getDescription());
            database.insert("Size", null, cv);
            Shelf.getHashSizes().put(s.getCode(), s);
        }
    }

    public void updateSizes(List<Size> sizes){
        for(Size s : sizes){
            ContentValues cv = new ContentValues();
            cv.put("Name", s.getName());
            cv.put("Description", s.getDescription());
            String[] whereArgs = new String[] {s.getCode()};
            database.update("Size", cv, "Code=?", whereArgs);
            Shelf.getHashSizes().put(s.getCode(), s);
        }
    }

    public void deleteSizes(List<Size> sizes){
        for(Size s : sizes){
            String[] whereArgs = new String[] {s.getCode()};
            database.delete("Size","Code=?", whereArgs);
            Shelf.getHashSizes().remove(s.getCode());
        }
    }


    public void insertSubcategories(List<Subcategory> subcategories){
        for(Subcategory s : subcategories){
            ContentValues cv = new ContentValues();
            cv.put("Code" , s.getCode());
            cv.put("Name", s.getName());
            cv.put("Description", s.getDescription());
            database.insert("Subcategory", null, cv);
            Shelf.getHashSubcategories().put(s.getCode(), s);
        }
    }

    public void updateSubcategories(List<Subcategory> subcategories){
        for(Subcategory s : subcategories){
            ContentValues cv = new ContentValues();
            cv.put("Name", s.getName());
            cv.put("Description", s.getDescription());
            String[] whereArgs = new String[] {s.getCode()};
            database.update("Subcategory", cv, "Code=?", whereArgs);
            Shelf.getHashSubcategories().put(s.getCode(), s);
        }
    }

    public void deleteSubcategories(List<Subcategory> subcategories){
        for(Subcategory s : subcategories){
            String[] whereArgs = new String[] {s.getCode()};
            database.delete("Subcategory","Code=?", whereArgs);
            Shelf.getHashSubcategories().remove(s.getCode());
        }
    }


    public void insertProductsXSizes(List<ProductXSize> productsXSizes){
        for(ProductXSize p : productsXSizes){
            ContentValues cv = new ContentValues();
            cv.put("ProductCode", p.getProductCode());
            cv.put("SizeCode", p.getSizeCode());
            cv.put("Price" , p.getPrice().doubleValue());
            database.insert("ProductXSize", null, cv);
            if(!Shelf.getHashProductsXSizes().containsKey(p.getProductCode()))
                Shelf.getHashProductsXSizes().put(p.getProductCode(), new ArrayList<ProductXSize>());
            Shelf.getHashProductsXSizes().get(p.getProductCode()).add(p);
        }
    }

    public void updateProductsXSizes(List<ProductXSize> productsXSizes){
        for(ProductXSize p : productsXSizes){
            ContentValues cv = new ContentValues();
            cv.put("Price" , p.getPrice().doubleValue());
            String[] whereArgs = new String[] {p.getProductCode(), p.getSizeCode()};
            database.update("ProductXSize", cv, "ProductCode=? AND SizeCode=?", whereArgs);
            List<ProductXSize> shelfProductsXSizes = Shelf.getHashProductsXSizes().get(p.getProductCode());
            for(ProductXSize p2 : shelfProductsXSizes){
                if(p2.getSizeCode().equals(p.getSizeCode())){
                    shelfProductsXSizes.remove(p2);
                    break;
                }
            }
            shelfProductsXSizes.add(p);
        }
    }

    public void deleteProductsXSizes(List<ProductXSize> productsXSizes){
        for(ProductXSize p : productsXSizes){
            String[] whereArgs = new String[] {p.getProductCode(), p.getSizeCode()};
            database.delete("ProductXSize","ProductCode=? AND SizeCode=?", whereArgs);
            List<ProductXSize> shelfProductsXSizes = Shelf.getHashProductsXSizes().get(p.getProductCode());
            for(ProductXSize p2 : shelfProductsXSizes){
                if(p2.getSizeCode().equals(p.getSizeCode())){
                    shelfProductsXSizes.remove(p2);
                    break;
                }
            }
            if(shelfProductsXSizes.size() == 0)
                Shelf.getHashProductsXSizes().remove(p.getProductCode());
        }
    }
}

