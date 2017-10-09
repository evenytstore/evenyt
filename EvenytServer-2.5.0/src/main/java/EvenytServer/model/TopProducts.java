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

public class TopProducts {
    @com.google.gson.annotations.SerializedName("idTopProducts")
    private BigDecimal idTopProducts = null;
    @com.google.gson.annotations.SerializedName("codeProduct")
    private String codeProduct = null;
    @com.google.gson.annotations.SerializedName("codeSize")
    private String codeSize = null;

    /**
     * Gets idTopProducts
     *
     * @return idTopProducts
     **/
    public BigDecimal getIdTopProducts() {
        return idTopProducts;
    }

    /**
     * Sets the value of idTopProducts.
     *
     * @param idTopProducts the new value
     */
    public void setIdTopProducts(BigDecimal idTopProducts) {
        this.idTopProducts = idTopProducts;
    }

    /**
     * Gets codeProduct
     *
     * @return codeProduct
     **/
    public String getCodeProduct() {
        return codeProduct;
    }

    /**
     * Sets the value of codeProduct.
     *
     * @param codeProduct the new value
     */
    public void setCodeProduct(String codeProduct) {
        this.codeProduct = codeProduct;
    }

    /**
     * Gets codeSize
     *
     * @return codeSize
     **/
    public String getCodeSize() {
        return codeSize;
    }

    /**
     * Sets the value of codeSize.
     *
     * @param codeSize the new value
     */
    public void setCodeSize(String codeSize) {
        this.codeSize = codeSize;
    }

}
