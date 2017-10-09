package com.app.evenytstore.Model;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.app.evenytstore.Activity.InitialActivity;
import com.app.evenytstore.Activity.LoadingActivity;
import com.app.evenytstore.R;
import com.app.evenytstore.Server.ServerAccess;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import EvenytServer.EvenytStoreAPIClient;
import EvenytServer.model.Brand;
import EvenytServer.model.BrandForm;
import EvenytServer.model.Category;
import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;
import EvenytServer.model.Size;
import EvenytServer.model.TopProducts;
import EvenytServer.model.Subcategory;

/**
 * Created by Enrique on 25/07/2017.
 */

public class ServerSynchronizeTask extends AsyncTask<Context, Integer, Void> {
    Context activity;
    ProgressBar pbarProgreso;
    public void setProgressBar(ProgressBar bar){
        pbarProgreso = bar;
    }
    @Override
    protected Void doInBackground(Context... params) {
        int countP=0;
        try {
            EvenytStoreAPIClient client = ServerAccess.getClient();

            activity = params[0];
            DatabaseAccess access = DatabaseAccess.getInstance(activity);

            List<Brand> newBrands = new ArrayList<>();
            List<Brand> updatedBrands = new ArrayList<>();
            List<Brand> deletedBrands = new ArrayList<>();

            List<Brand> serverBrands = client.brandsGet();
            for (Brand b : serverBrands) {
                countP++;

                if (Shelf.getHashBrands().containsKey(b.getCode())) {
                    Brand b2 = Shelf.getHashBrands().get(b.getCode());
                    if (!b.getDescription().equals(b2.getDescription()) || !b.getName().equals(b2.getName()))
                        updatedBrands.add(b);
                } else
                    newBrands.add(b);
            }
            publishProgress(countP);
            for (Brand b : Shelf.getHashBrands().values()) {
                boolean found = false;

                for (Brand b2 : serverBrands) {
                    countP++;

                    if (b2.getCode().equals(b.getCode())) {
                        found = true;
                        break;
                    }
                }

                if (!found)
                    deletedBrands.add(b);
            }
            publishProgress(countP);
            access.open();
            access.insertBrands(newBrands);
            access.updateBrands(updatedBrands);
            access.deleteBrands(deletedBrands);
            access.close();

            List<BrandForm> newBrandForms = new ArrayList<>();
            List<BrandForm> updatedBrandForms = new ArrayList<>();
            List<BrandForm> deletedBrandForms = new ArrayList<>();

            List<BrandForm> serverBrandForms = client.brandFormsGet();
            for (BrandForm b : serverBrandForms) {
                countP++;

                if (Shelf.getHashBrandForms().containsKey(b.getCode())) {
                    BrandForm b2 = Shelf.getHashBrandForms().get(b.getCode());
                    if (!b.getDescription().equals(b2.getDescription()) || !b.getName().equals(b2.getName()))
                        updatedBrandForms.add(b);
                } else
                    newBrandForms.add(b);
            }
            publishProgress(countP);
            for (BrandForm b : Shelf.getHashBrandForms().values()) {
                boolean found = false;

                for (BrandForm b2 : serverBrandForms) {
                    countP++;

                    if (b2.getCode().equals(b.getCode())) {
                        found = true;
                        break;
                    }
                }

                if (!found)
                    deletedBrandForms.add(b);
            }
            publishProgress(countP);
            access.open();
            access.insertBrandForms(newBrandForms);
            access.updateBrandForms(updatedBrandForms);
            access.deleteBrandForms(deletedBrandForms);
            access.close();

            List<Category> newCategories = new ArrayList<>();
            List<Category> updatedCategories = new ArrayList<>();
            List<Category> deletedCategories = new ArrayList<>();

            List<Category> serverCategories = client.categoriesGet();
            for (Category c : serverCategories) {
                countP++;

                if (Shelf.getHashCategories().containsKey(c.getCode())) {
                    Category c2 = Shelf.getHashCategories().get(c.getCode());
                    if (!c.getDescription().equals(c2.getDescription()) || !c.getName().equals(c2.getName()))
                        updatedCategories.add(c);
                } else
                    newCategories.add(c);
            }
            publishProgress(countP);
            for (Category c : Shelf.getHashCategories().values()) {
                boolean found = false;

                for (Category c2 : serverCategories) {
                    countP++;

                    if (c2.getCode().equals(c.getCode())) {
                        found = true;
                        break;
                    }
                }

                if (!found)
                    deletedCategories.add(c);
            }
            publishProgress(countP);
            access.open();
            access.insertCategories(newCategories);
            access.updateCategories(updatedCategories);
            access.deleteCategories(deletedCategories);
            access.close();

            List<Subcategory> newSubcategories = new ArrayList<>();
            List<Subcategory> updatedSubcategories = new ArrayList<>();
            List<Subcategory> deletedSubcategories = new ArrayList<>();

            List<Subcategory> serverSubcategories = client.subcategoriesGet();
            for (Subcategory s : serverSubcategories) {
                countP++;

                if (Shelf.getHashSubcategories().containsKey(s.getCode())) {
                    Subcategory s2 = Shelf.getHashSubcategories().get(s.getCode());
                    if (!s.getDescription().equals(s2.getDescription()) || !s.getName().equals(s2.getName()))
                        updatedSubcategories.add(s);
                } else
                    newSubcategories.add(s);
            }
            publishProgress(countP);
            for (Subcategory s : Shelf.getHashSubcategories().values()) {
                boolean found = false;

                for (Subcategory s2 : serverSubcategories) {
                    countP++;

                    if (s2.getCode().equals(s.getCode())) {
                        found = true;
                        break;
                    }
                }

                if (!found)
                    deletedSubcategories.add(s);
            }
            publishProgress(countP);
            access.open();
            access.insertSubcategories(newSubcategories);
            access.updateSubcategories(updatedSubcategories);
            access.deleteSubcategories(deletedSubcategories);
            access.close();

            List<Product> newProducts = new ArrayList<>();
            List<Product> updatedProducts = new ArrayList<>();
            List<Product> deletedProducts = new ArrayList<>();

            List<Product> serverProducts = client.productsGet();
            for (Product p : serverProducts) {
                countP++;

                if (Shelf.getHashProducts().containsKey(p.getCode())) {
                    Product p2 = Shelf.getHashProducts().get(p.getCode());
                    if (!p.getDateLastUpdate().equals(p2.getDateLastUpdate()))
                        updatedProducts.add(p);
                } else
                    newProducts.add(p);
            }
            publishProgress(countP);
            for (Product p : Shelf.getHashProducts().values()) {
                boolean found = false;

                for (Product p2 : serverProducts) {
                    countP++;

                    if (p2.getCode().equals(p.getCode())) {
                        found = true;
                        break;
                    }
                }

                if (!found)
                    deletedProducts.add(p);
            }
            publishProgress(countP);
            access.open();
            access.insertProducts(newProducts);
            access.updateProducts(updatedProducts);
            access.deleteProducts(deletedProducts);
            access.close();

            List<Size> newSizes = new ArrayList<>();
            List<Size> updatedSizes = new ArrayList<>();
            List<Size> deletedSizes = new ArrayList<>();

            List<Size> serverSizes = client.sizesGet();
            for (Size s : serverSizes) {
                countP++;

                if (Shelf.getHashSizes().containsKey(s.getCode())) {
                    Size s2 = Shelf.getHashSizes().get(s.getCode());
                    if (!s.getDescription().equals(s2.getDescription()) || !s.getName().equals(s2.getName()))
                        updatedSizes.add(s);
                } else
                    newSizes.add(s);
            }
            publishProgress(countP);
            for (Size s : Shelf.getHashSizes().values()) {
                boolean found = false;

                for (Size s2 : serverSizes) {
                    countP++;

                    if (s2.getCode().equals(s.getCode())) {
                        found = true;
                        break;
                    }
                }

                if (!found)
                    deletedSizes.add(s);
            }
            publishProgress(countP);
            access.open();
            access.insertSizes(newSizes);
            access.updateSizes(updatedSizes);
            access.deleteSizes(deletedSizes);
            access.close();

            List<ProductXSize> newProductsXSizes = new ArrayList<>();
            List<ProductXSize> updatedProductsXSizes = new ArrayList<>();
            List<ProductXSize> deletedProductsXSizes = new ArrayList<>();

            List<ProductXSize> serverProductsXSizes = client.productsXsizesGet();
            for (ProductXSize p : serverProductsXSizes) {

                if (Shelf.getProductsToSizes().containsKey(p.getProductCode())) {
                    List<ProductXSize> shelfProductsXSizes = Shelf.getProductsToSizes().get(p.getProductCode());
                    boolean found = false;
                    for(ProductXSize p2 : shelfProductsXSizes){
                        countP++;

                        if(p2.getSizeCode().equals(p.getSizeCode())){
                            if (p.getPrice().compareTo(p2.getPrice()) != 0)
                                updatedProductsXSizes.add(p);
                            found = true;
                            break;
                        }
                    }
                    if(!found)
                        newProductsXSizes.add(p);
                } else
                    newProductsXSizes.add(p);
            }
            publishProgress(countP);
            for (List<ProductXSize> productXSizes : Shelf.getProductsToSizes().values()) {
                for(ProductXSize p : productXSizes){
                    boolean found = false;

                    for (ProductXSize p2 : serverProductsXSizes) {
                        countP++;

                        if (p2.getProductCode().equals(p.getProductCode()) && p2.getSizeCode().equals(p.getSizeCode())) {
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                        deletedProductsXSizes.add(p);
                }
            }
            publishProgress(countP);
            access.open();
            access.insertProductsXSizes(newProductsXSizes);
            access.updateProductsXSizes(updatedProductsXSizes);
            access.deleteProductsXSizes(deletedProductsXSizes);
            access.close();

            List<TopProducts> topProducts = new ArrayList<>();

            List<TopProducts> serverTopProducts = client.topproductsGet();

            for (TopProducts s : serverTopProducts) {

                Shelf.getTopProducts().add(s);

            }



        }catch(Exception e){
            Log.d("Error", e.toString());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        int progreso = values[0].intValue();
        pbarProgreso.setProgress(progreso);
    }

    @Override
    protected void onPreExecute() {
        pbarProgreso.setMax(2656364);
        pbarProgreso.setProgress(5);
    }
    protected void onPostExecute(Void result){


        Intent intent = new Intent(activity, InitialActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}