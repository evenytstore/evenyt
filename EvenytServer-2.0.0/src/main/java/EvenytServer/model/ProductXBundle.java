/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package EvenytServer.model;

import EvenytServer.model.Address;
import java.math.BigDecimal;

public class ProductXBundle {
    @com.google.gson.annotations.SerializedName("Product_idProduct")
    private String productIdProduct = null;
    @com.google.gson.annotations.SerializedName("Bundle_idBundle")
    private Integer bundleIdBundle = null;
    @com.google.gson.annotations.SerializedName("quantity")
    private Integer quantity = null;
    @com.google.gson.annotations.SerializedName("dateDefault")
    private String dateDefault = null;
    @com.google.gson.annotations.SerializedName("dateOrder")
    private String dateOrder = null;
    @com.google.gson.annotations.SerializedName("address")
    private Address address = null;
    @com.google.gson.annotations.SerializedName("Subtotal")
    private BigDecimal subtotal = null;
    @com.google.gson.annotations.SerializedName("productSize")
    private String productSize = null;

    /**
     * Gets productIdProduct
     *
     * @return productIdProduct
     **/
    public String getProductIdProduct() {
        return productIdProduct;
    }

    /**
     * Sets the value of productIdProduct.
     *
     * @param productIdProduct the new value
     */
    public void setProductIdProduct(String productIdProduct) {
        this.productIdProduct = productIdProduct;
    }

    /**
     * Gets bundleIdBundle
     *
     * @return bundleIdBundle
     **/
    public Integer getBundleIdBundle() {
        return bundleIdBundle;
    }

    /**
     * Sets the value of bundleIdBundle.
     *
     * @param bundleIdBundle the new value
     */
    public void setBundleIdBundle(Integer bundleIdBundle) {
        this.bundleIdBundle = bundleIdBundle;
    }

    /**
     * Gets quantity
     *
     * @return quantity
     **/
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of quantity.
     *
     * @param quantity the new value
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets dateDefault
     *
     * @return dateDefault
     **/
    public String getDateDefault() {
        return dateDefault;
    }

    /**
     * Sets the value of dateDefault.
     *
     * @param dateDefault the new value
     */
    public void setDateDefault(String dateDefault) {
        this.dateDefault = dateDefault;
    }

    /**
     * Gets dateOrder
     *
     * @return dateOrder
     **/
    public String getDateOrder() {
        return dateOrder;
    }

    /**
     * Sets the value of dateOrder.
     *
     * @param dateOrder the new value
     */
    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    /**
     * Gets address
     *
     * @return address
     **/
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the value of address.
     *
     * @param address the new value
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Gets subtotal
     *
     * @return subtotal
     **/
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    /**
     * Sets the value of subtotal.
     *
     * @param subtotal the new value
     */
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Gets productSize
     *
     * @return productSize
     **/
    public String getProductSize() {
        return productSize;
    }

    /**
     * Sets the value of productSize.
     *
     * @param productSize the new value
     */
    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

}
