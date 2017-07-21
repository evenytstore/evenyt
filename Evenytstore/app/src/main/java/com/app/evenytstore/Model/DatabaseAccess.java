package com.app.evenytstore.Model;

/**
 * Created by Enrique on 14/07/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.evenytstore.Utility.DateHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    public static String TOKEN_HTTP;

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
        //ArrayList<Product> products = new ArrayList<>();
        HashMap  hashMap = new HashMap<String,Customer>();
        Cursor cursor = database.rawQuery("SELECT * FROM Customer", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Customer c = new Customer();
            c.setId(cursor.getString(0));
            c.setEmail(cursor.getString(1));
            c.setPhone(cursor.getString(2));
            c.setName(cursor.getString(3));
            c.setLastName(cursor.getString(4));
            c.setAddress(cursor.getString(5));
            c.setBirthday(DateHandler.toDate(cursor.getString(6)));
            c.setDni(cursor.getString(7));
            c.setRuc(cursor.getString(8));
            //products.add(p);
            hashMap.put(c.getId(),c);
            cursor.moveToNext();
        }
        return hashMap;
    }


    public void insertCustomer(Customer c){
        ContentValues cv = new ContentValues();
        cv.put("id" , c.getId());
        cv.put("email", c.getEmail());
        cv.put("phone", c.getPhone());
        cv.put("name", c.getName());
        cv.put("lastName", c.getLastName());
        cv.put("address", c.getAddress());
        cv.put("birthday", DateHandler.toString(c.getBirthday()));
        cv.put("dni", c.getDni());
        cv.put("ruc", c.getRuc());
        database.insert("Customer", null, cv);
        //Shelf.getHashSubcategories().put(s.getCode(), s);
    }

    /**
     * Read all quotes from the database.
     *
     private String code;
     private Category category;
     private String size;
     private String name;
     private double price;
     private int thumbnail;
     private String description;
     private Calendar creationDate;
     private Calendar updateDate;
     private int status;*/
    /*public HashMap<String, ArrayList<Product>> getProducts() throws ParseException {
        final HashMap products = new HashMap<String, ArrayList<Product>>();
        HashMap<String, Category> selects = Shelf.getHashCategories();
        for(Map.Entry<String, Category> entry : selects.entrySet()) {
            String key = entry.getKey();
            products.put(key,new ArrayList<Product>());
        }

        Cursor cursor = database.rawQuery("SELECT * FROM Products", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product p= new Product();

            p.setCode(cursor.getString(0));
            if(Shelf.getHashCategories().containsKey(cursor.getString(1)))
                p.setCategory((Category) Shelf.getHashCategories().get(cursor.getString(1)));
            if(Shelf.getHashSubcategories().containsKey(cursor.getString(2)))
                p.setSubcategory((Subcategory) Shelf.getHashSubcategories().get(cursor.getString(2)));

            //p.setImage(cursor.getBlob(2));
            p.setImagePath(cursor.getString(3));
            p.setSize(cursor.getString(4));
            p.setName(cursor.getString(5));
            p.setPrice(Double.parseDouble(cursor.getString(6)));
            p.setDescription(cursor.getString(7));
            Calendar cal= Calendar.getInstance();
            SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            cal.setTime(sdf.parse(cursor.getString(8)));// all done
            p.setCreationDate(cal);
            Calendar cal1= Calendar.getInstance();
            cal1.setTime(sdf.parse(cursor.getString(9)));
            p.setStatus(cursor.getString(10));
            p.setId(Integer.parseInt(cursor.getString(11)));

            p.setNx1(Integer.parseInt(cursor.getString(12)));
            p.setDiscount(Double.parseDouble(cursor.getString(13)));

            if(p.getCategory() != null)
                ((ArrayList<Product>) products.get(p.getCategory().getCode())).add(p);
            cursor.moveToNext();
        }
        cursor.close();
        return products;
    }

    public HashMap<String,Subcategory> getSubcategories(){
        HashMap hashMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Subcategory", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Subcategory s = new Subcategory();
            s.setCode(cursor.getString(0));
            s.setName(cursor.getString(1));

            hashMap.put(s.getCode(), s);
            cursor.moveToNext();
        }
        cursor.close();
        return hashMap;
    }

    public HashMap<String,Category> getCategories(){
        HashMap hashMap = new HashMap<String,Category>();
        Cursor cursor = database.rawQuery("SELECT * FROM Category", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category p= new Category();
            p.setCode(cursor.getString(0));
            p.setName(cursor.getString(1));

            hashMap.put(p.getCode(),p);
            cursor.moveToNext();
        }
        cursor.close();
        return hashMap;
    }


    public HashMap<String,Product> getAllProds() throws ParseException{
        //ArrayList<Product> products = new ArrayList<>();
        HashMap  hashMap = new HashMap<String,Product>();
        Cursor cursor = database.rawQuery("SELECT * FROM Products", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product p= new Product();
            p.setCode(cursor.getString(0));
            p.setCategory((Category) Shelf.getHashCategories().get(cursor.getString(1)));
            p.setSubcategory((Subcategory) Shelf.getHashSubcategories().get(cursor.getString(2)));

            //p.setImage(cursor.getBlob(2));
            p.setImagePath(cursor.getString(3));
            p.setSize(cursor.getString(4));
            p.setName(cursor.getString(5));
            p.setPrice(Double.parseDouble(cursor.getString(6)));
            p.setDescription(cursor.getString(7));
            Calendar cal= Calendar.getInstance();
            SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            cal.setTime(sdf.parse(cursor.getString(8)));// all done
            p.setCreationDate(cal);
            Calendar cal1= Calendar.getInstance();
            cal1.setTime(sdf.parse(cursor.getString(9)));
            p.setStatus(cursor.getString(10));
            p.setId(Integer.parseInt(cursor.getString(11)));
            p.setNx1(Integer.parseInt(cursor.getString(12)));
            p.setDiscount(Double.parseDouble(cursor.getString(13)));
            //products.add(p);
            hashMap.put(p.getCode(),p);
            cursor.moveToNext();
        }
        return hashMap;
    }

    public HashMap<Integer,UnitType> getAllUnitTypes()throws ParseException {
        HashMap hashMap = new HashMap<Integer,UnitType>();
        Cursor cursor = database.rawQuery("SELECT * FROM UnitType", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            UnitType p= new UnitType();
            p.setId(Integer.parseInt(cursor.getString(0)));
            p.setName(cursor.getString(1));
            p.setDescription(cursor.getString(2));
            p.setSymbol(cursor.getString(3));

            hashMap.put(p.getId(),p);
            cursor.moveToNext();
        }
        cursor.close();
        return hashMap;
    }

    public HashMap<Integer,FinalProductType> getAllProductTypes()throws ParseException{
        HashMap hashMap = new HashMap<Integer,FinalProductType>();
        Cursor cursor = database.rawQuery("SELECT * FROM ProductType", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FinalProductType p= new FinalProductType();
            p.setId(Integer.parseInt(cursor.getString(0)));
            p.setName(cursor.getString(1));
            p.setDescription(cursor.getString(2));
            p.setSalePrice(cursor.getDouble(3));
            p.setWeight(cursor.getDouble(4));
            p.setImgPath(cursor.getString(5));
            p.setUnitTypeId(cursor.getInt(6));
            //p.setUsesKg();
            if(cursor.getInt(7)==1){
                p.setUsesKg(true);
            }else {
                p.setUsesKg(false);
            }

            hashMap.put(p.getId(),p);
            cursor.moveToNext();
        }
        cursor.close();
        return hashMap;
    }


    public void insertSubsectors(List<Category> categories){
        for(Category s : categories){
            ContentValues cv = new ContentValues();
            cv.put("ID" , s.getCode());
            cv.put("Category", s.getName());
            database.insert("Category", null, cv);
            Shelf.getHashCategories().put(s.getCode(), s);
        }
    }

    public void updateSubsectors(List<Category> categories){
        for(Category s : categories){
            ContentValues cv = new ContentValues();
            cv.put("Category",s.getName());
            String[] whereArgs = new String[] {s.getCode()};
            database.update("Category", cv, "ID=?", whereArgs);
            Shelf.getHashCategories().put(s.getCode(), s);
        }
    }

    public void deleteSubsectors(List<Category> categories){
        for(Category s : categories){
            String[] whereArgs = new String[] {s.getCode()};
            database.delete("Category","ID=?", whereArgs);
            Shelf.getHashCategories().remove(s.getCode());
        }
    }

    public void insertSubcategories(List<Subcategory> subcategories){
        for(Subcategory s : subcategories){
            ContentValues cv = new ContentValues();
            cv.put("ID" , s.getCode());
            cv.put("Name", s.getName());
            database.insert("Subcategory", null, cv);
            Shelf.getHashSubcategories().put(s.getCode(), s);
        }
    }

    public void updateSubcategories(List<Subcategory> subcategories){
        for(Subcategory s : subcategories){
            ContentValues cv = new ContentValues();
            cv.put("Category",s.getName());
            String[] whereArgs = new String[] {s.getCode()};
            database.update("Subcategory", cv, "ID=?", whereArgs);
            Shelf.getHashSubcategories().put(s.getCode(), s);
        }
    }

    public void deleteSubcategories(List<Subcategory> categories){
        for(Subcategory s : categories){
            String[] whereArgs = new String[] {s.getCode()};
            database.delete("Subcategory","ID=?", whereArgs);
            Shelf.getHashSubcategories().remove(s.getCode());
        }
    }

    public void insertProducts(List<Product> products){
        for(Product p : products){
            ContentValues cv = new ContentValues();
            cv.put("id",p.getId());
            cv.put("Code", p.getCode());
            cv.put("CategoryID", p.getCategory().getCode());
            cv.put("Size", p.getSize());
            cv.put("Name", p.getName());
            cv.put("Discount", p.getDiscount());
            cv.put("Nx1", p.getNx1());
            cv.put("Price", String.valueOf(p.getPrice()));
            cv.put("Description", p.getDescription());
            SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String creationDateString = sdf.format(p.getCreationDate().getTime());
            String updateDateString = sdf.format(p.getUpdateDate().getTime());
            cv.put("CreationDate", creationDateString);
            cv.put("UpdateDate", updateDateString);
            cv.put("Status", p.getStatus());
            cv.put("ImagePath", p.getImagePath());
            database.insert("Products", null, cv);
//
            if(!Shelf.getHashProducts().containsKey(p.getCategory().getCode()))
                Shelf.getHashProducts().put(p.getCategory().getCode(), new ArrayList<Product>());
            List<Product> currentProducts = (List<Product>) Shelf.getHashProducts().get(p.getCategory().getCode());
            currentProducts.add(p);
            Shelf.getAllProducts().put(p.getCode(), p);
        }
    }

    public void updateProducts(List<Product> products){
        for(Product p : products){
            ContentValues cv = new ContentValues();
            cv.put("id",p.getId());
            cv.put("CategoryID", p.getCategory().getCode());
            cv.put("Size", p.getSize());
            cv.put("Name", p.getName());
            cv.put("Discount", p.getDiscount());
            cv.put("Nx1", p.getNx1());
            cv.put("Price", String.valueOf(p.getPrice()));
            cv.put("Description", p.getDescription());
            SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String updateDateString = sdf.format(p.getUpdateDate().getTime());
            cv.put("UpdateDate", updateDateString);
            cv.put("Status", p.getStatus());
            cv.put("ImagePath", p.getImagePath());
            String[] whereArgs = new String[] {p.getCode()};
            database.update("Products", cv, "Code=?", whereArgs);
            List<Product> currentProducts = (List<Product>) Shelf.getHashProducts().get(p.getCategory().getCode());
            int i = 0;
            for(Product p2 : currentProducts){
                if(p2.getCode().equals(p.getCode())){
                    break;
                }
                i += 1;
            }
            currentProducts.remove(i);
            currentProducts.add(p);
            Shelf.getAllProducts().put(p.getCode(), p);
        }
    }

    public void deleteProducts(List<Product> products){
        for(Product p : products){
            String[] whereArgs = new String[] {p.getCode()};
            database.delete("Product","Code=?", whereArgs);
            Shelf.getHashProducts().remove(p.getCode());
            if(p.getCategory() != null){
                List<Product> currentProducts = (List<Product>) Shelf.getHashProducts().get(p.getCategory().getCode());
                int i = 0;
                for(Product p2 : currentProducts){
                    if(p2.getCode().equals(p.getCode())){
                        break;
                    }
                    i += 1;
                }
                currentProducts.remove(p);
            }
            Shelf.getAllProducts().remove(p.getCode());
        }
    }

    public void updateImageProduct(String code, byte[] image){
        ContentValues cv = new ContentValues();
        cv.put("Image", image);
        database.update("Products", cv, "Code = "+code, null);
    }

    public void insertProductTypes(List<FinalProductType> finalProductTypes){
        for (FinalProductType f: finalProductTypes){
            ContentValues cv = new ContentValues();
            cv.put("id",f.getId());
            cv.put("Name",f.getName());
            cv.put("Description",f.getDescription());
            cv.put("Sale_Price",f.getSalePrice());
            cv.put("Weight",f.getWeight());
            cv.put("imgPath",f.getImgPath());
            cv.put("Unit_Type",f.getUnitTypeId());
            if(f.isUsesKg()){
                cv.put("UsesKg",1);
            }else{
                cv.put("UsesKg",0);
            }
            database.insert("ProductType", null, cv);
            Shelf.getProductTypes().put(f.getId(),f);
        }
    }

    public void updateProductTypes(List<FinalProductType> finalProductTypes){
        for (FinalProductType f: finalProductTypes){
            ContentValues cv = new ContentValues();
            cv.put("id",f.getId());
            cv.put("Name",f.getName());
            cv.put("Description",f.getDescription());
            cv.put("Sale_Price",f.getSalePrice());
            cv.put("Weight",f.getWeight());
            cv.put("imgPath",f.getImgPath());
            cv.put("Unit_Type",f.getUnitTypeId());
            //database.insert("Products", null, cv);
            String[] whereArgs = new String[] {String.valueOf(f.getId())};
            database.update("ProductType",cv,"id=?", whereArgs);
            Shelf.getProductTypes().put(f.getId(),f);
        }
    }

    public void deleteProductTypes(List<FinalProductType> finalProductTypes){
        for(FinalProductType f : finalProductTypes){
            String[] whereArgs = new String[] {String.valueOf(f.getId())};
            database.delete("ProductType","id=?", whereArgs);
            Shelf.getProductTypes().remove(f.getId());
        }
    }

    public void insertUnitTypes(List<UnitType> unitTypes){
        for (UnitType u:unitTypes){
            ContentValues cv = new ContentValues();
            cv.put("id",u.getId());
            cv.put("Name",u.getName());
            cv.put("Description",u.getDescription());
            cv.put("Symbol",u.getSymbol());
            database.insert("UnitType",null,cv);
            Shelf.getUnityTypes().put(u.getId(),u);
        }
    }

    public void updateUnitTypes(List<UnitType> unitTypes){
        for (UnitType u:unitTypes){
            ContentValues cv = new ContentValues();
            cv.put("id",u.getId());
            cv.put("Name",u.getName());
            cv.put("Description",u.getDescription());
            cv.put("Symbol",u.getSymbol());
            //database.insert("UnitType",null,cv);
            String[] whereArgs = new String[] {String.valueOf(u.getId())};
            database.update("UnitType",cv,"id=?",whereArgs);
            Shelf.getUnityTypes().put(u.getId(),u);
        }
    }


    public void deleteUnitTypes(List<UnitType> unitTypes){
        for(UnitType u : unitTypes){
            String[] whereArgs = new String[] {String.valueOf(u.getId())};
            database.delete("UnitType","id=?", whereArgs);
            Shelf.getUnityTypes().remove(u.getId());
        }
    }*/

}

