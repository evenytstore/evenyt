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

public class ProductXSize {
    @com.google.gson.annotations.SerializedName("Product_code")
    private String productCode = null;
    @com.google.gson.annotations.SerializedName("Size_code")
    private String sizeCode = null;
    @com.google.gson.annotations.SerializedName("price")
    private BigDecimal price = null;

    /**
     * Gets productCode
     *
     * @return productCode
     **/
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the value of productCode.
     *
     * @param productCode the new value
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * Gets sizeCode
     *
     * @return sizeCode
     **/
    public String getSizeCode() {
        return sizeCode;
    }

    /**
     * Sets the value of sizeCode.
     *
     * @param sizeCode the new value
     */
    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    /**
     * Gets price
     *
     * @return price
     **/
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the value of price.
     *
     * @param price the new value
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
