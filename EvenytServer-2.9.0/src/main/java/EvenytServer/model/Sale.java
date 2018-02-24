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

import java.math.BigDecimal;
import EvenytServer.model.Bundle;
import EvenytServer.model.Promotions;

public class Sale {
    @com.google.gson.annotations.SerializedName("idSale")
    private Integer idSale = null;
    @com.google.gson.annotations.SerializedName("total")
    private BigDecimal total = null;
    @com.google.gson.annotations.SerializedName("rating")
    private BigDecimal rating = null;
    @com.google.gson.annotations.SerializedName("status")
    private Integer status = null;
    @com.google.gson.annotations.SerializedName("bundle")
    private Bundle bundle = null;
    @com.google.gson.annotations.SerializedName("promotion")
    private Promotions promotion = null;
    @com.google.gson.annotations.SerializedName("Bundle_Customer_idCustomer")
    private String bundleCustomerIdCustomer = null;
    @com.google.gson.annotations.SerializedName("typeSale_idtypeSale")
    private Integer typeSaleIdtypeSale = null;
    @com.google.gson.annotations.SerializedName("Evener_idEvener")
    private Integer evenerIdEvener = null;
    @com.google.gson.annotations.SerializedName("typePayment")
    private Integer typePayment = null;
    @com.google.gson.annotations.SerializedName("amountToPay")
    private BigDecimal amountToPay = null;

    /**
     * Gets idSale
     *
     * @return idSale
     **/
    public Integer getIdSale() {
        return idSale;
    }

    /**
     * Sets the value of idSale.
     *
     * @param idSale the new value
     */
    public void setIdSale(Integer idSale) {
        this.idSale = idSale;
    }

    /**
     * Gets total
     *
     * @return total
     **/
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * Sets the value of total.
     *
     * @param total the new value
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * Gets rating
     *
     * @return rating
     **/
    public BigDecimal getRating() {
        return rating;
    }

    /**
     * Sets the value of rating.
     *
     * @param rating the new value
     */
    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    /**
     * Gets status
     *
     * @return status
     **/
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets the value of status.
     *
     * @param status the new value
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Gets bundle
     *
     * @return bundle
     **/
    public Bundle getBundle() {
        return bundle;
    }

    /**
     * Sets the value of bundle.
     *
     * @param bundle the new value
     */
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    /**
     * Gets promotion
     *
     * @return promotion
     **/
    public Promotions getPromotion() {
        return promotion;
    }

    /**
     * Sets the value of promotion.
     *
     * @param promotion the new value
     */
    public void setPromotion(Promotions promotion) {
        this.promotion = promotion;
    }

    /**
     * Gets bundleCustomerIdCustomer
     *
     * @return bundleCustomerIdCustomer
     **/
    public String getBundleCustomerIdCustomer() {
        return bundleCustomerIdCustomer;
    }

    /**
     * Sets the value of bundleCustomerIdCustomer.
     *
     * @param bundleCustomerIdCustomer the new value
     */
    public void setBundleCustomerIdCustomer(String bundleCustomerIdCustomer) {
        this.bundleCustomerIdCustomer = bundleCustomerIdCustomer;
    }

    /**
     * Gets typeSaleIdtypeSale
     *
     * @return typeSaleIdtypeSale
     **/
    public Integer getTypeSaleIdtypeSale() {
        return typeSaleIdtypeSale;
    }

    /**
     * Sets the value of typeSaleIdtypeSale.
     *
     * @param typeSaleIdtypeSale the new value
     */
    public void setTypeSaleIdtypeSale(Integer typeSaleIdtypeSale) {
        this.typeSaleIdtypeSale = typeSaleIdtypeSale;
    }

    /**
     * Gets evenerIdEvener
     *
     * @return evenerIdEvener
     **/
    public Integer getEvenerIdEvener() {
        return evenerIdEvener;
    }

    /**
     * Sets the value of evenerIdEvener.
     *
     * @param evenerIdEvener the new value
     */
    public void setEvenerIdEvener(Integer evenerIdEvener) {
        this.evenerIdEvener = evenerIdEvener;
    }

    /**
     * Gets typePayment
     *
     * @return typePayment
     **/
    public Integer getTypePayment() {
        return typePayment;
    }

    /**
     * Sets the value of typePayment.
     *
     * @param typePayment the new value
     */
    public void setTypePayment(Integer typePayment) {
        this.typePayment = typePayment;
    }

    /**
     * Gets amountToPay
     *
     * @return amountToPay
     **/
    public BigDecimal getAmountToPay() {
        return amountToPay;
    }

    /**
     * Sets the value of amountToPay.
     *
     * @param amountToPay the new value
     */
    public void setAmountToPay(BigDecimal amountToPay) {
        this.amountToPay = amountToPay;
    }

}
