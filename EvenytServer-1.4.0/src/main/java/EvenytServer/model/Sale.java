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

public class Sale {
    @com.google.gson.annotations.SerializedName("idSale")
    private Integer idSale = null;
    @com.google.gson.annotations.SerializedName("total")
    private BigDecimal total = null;
    @com.google.gson.annotations.SerializedName("rating")
    private BigDecimal rating = null;
    @com.google.gson.annotations.SerializedName("status")
    private Integer status = null;
    @com.google.gson.annotations.SerializedName("Bundle_idBundle")
    private Integer bundleIdBundle = null;
    @com.google.gson.annotations.SerializedName("Bundle_Customer_idCustomer")
    private Integer bundleCustomerIdCustomer = null;
    @com.google.gson.annotations.SerializedName("typeSale_idTypeSale")
    private Integer typeSaleIdTypeSale = null;
    @com.google.gson.annotations.SerializedName("Evener_idEvener")
    private Integer evenerIdEvener = null;

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
     * Gets bundleCustomerIdCustomer
     *
     * @return bundleCustomerIdCustomer
     **/
    public Integer getBundleCustomerIdCustomer() {
        return bundleCustomerIdCustomer;
    }

    /**
     * Sets the value of bundleCustomerIdCustomer.
     *
     * @param bundleCustomerIdCustomer the new value
     */
    public void setBundleCustomerIdCustomer(Integer bundleCustomerIdCustomer) {
        this.bundleCustomerIdCustomer = bundleCustomerIdCustomer;
    }

    /**
     * Gets typeSaleIdTypeSale
     *
     * @return typeSaleIdTypeSale
     **/
    public Integer getTypeSaleIdTypeSale() {
        return typeSaleIdTypeSale;
    }

    /**
     * Sets the value of typeSaleIdTypeSale.
     *
     * @param typeSaleIdTypeSale the new value
     */
    public void setTypeSaleIdTypeSale(Integer typeSaleIdTypeSale) {
        this.typeSaleIdTypeSale = typeSaleIdTypeSale;
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

}
