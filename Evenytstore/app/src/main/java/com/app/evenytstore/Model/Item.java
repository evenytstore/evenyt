package com.app.evenytstore.Model;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;
import EvenytServer.model.Size;

/**
 * Created by Enrique on 09/08/2017.
 */


public class Item {
    private Product product = null;
    private Size size = null;
    private ProductXSize productXSize = null;
    private int count;
    private double subtotal;
    private double minPrice;

    public Item(ProductXSize productXSize){
        this.productXSize = productXSize;
        this.product=Shelf.getProductByCode(productXSize.getProductCode());
        this.size = Shelf.getSizeByCode(productXSize.getSizeCode());
        count=0;
        this.subtotal=0;
    }

    public void sum(int add){
        setCount(getCount() + add);
        int count = getCount();
        setSubtotal(this.getPrice()*count);
    }

    public void sub(int add){
        setCount(getCount() - add);
        int count = getCount();
        setSubtotal(this.getPrice()*count);
    }

    public double getPrice(){
        return productXSize.getPrice().doubleValue();
    }

    public Product getProduct() {
        return product;
    }

    public ProductXSize getProductXSize(){return productXSize;}

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName(){
        if(product!=null)
            return product.getName();
        return "";
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
