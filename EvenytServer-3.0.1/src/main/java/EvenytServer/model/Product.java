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


public class Product {
    @com.google.gson.annotations.SerializedName("Category_code")
    private String categoryCode = null;
    @com.google.gson.annotations.SerializedName("code")
    private String code = null;
    @com.google.gson.annotations.SerializedName("name")
    private String name = null;
    @com.google.gson.annotations.SerializedName("SubCategory_code")
    private String subCategoryCode = null;
    @com.google.gson.annotations.SerializedName("BrandForm_code")
    private String brandFormCode = null;
    @com.google.gson.annotations.SerializedName("Brand_code")
    private String brandCode = null;
    @com.google.gson.annotations.SerializedName("imgSrc")
    private String imgSrc = null;
    @com.google.gson.annotations.SerializedName("DateLastUpdate")
    private String dateLastUpdate = null;
    @com.google.gson.annotations.SerializedName("shortDescription")
    private String shortDescription = null;
    @com.google.gson.annotations.SerializedName("description")
    private String description = null;

    /**
     * Gets categoryCode
     *
     * @return categoryCode
     **/
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * Sets the value of categoryCode.
     *
     * @param categoryCode the new value
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * Gets code
     *
     * @return code
     **/
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of code.
     *
     * @param code the new value
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets name
     *
     * @return name
     **/
    public String getName() {
        return name;
    }

    /**
     * Sets the value of name.
     *
     * @param name the new value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets subCategoryCode
     *
     * @return subCategoryCode
     **/
    public String getSubCategoryCode() {
        return subCategoryCode;
    }

    /**
     * Sets the value of subCategoryCode.
     *
     * @param subCategoryCode the new value
     */
    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    /**
     * Gets brandFormCode
     *
     * @return brandFormCode
     **/
    public String getBrandFormCode() {
        return brandFormCode;
    }

    /**
     * Sets the value of brandFormCode.
     *
     * @param brandFormCode the new value
     */
    public void setBrandFormCode(String brandFormCode) {
        this.brandFormCode = brandFormCode;
    }

    /**
     * Gets brandCode
     *
     * @return brandCode
     **/
    public String getBrandCode() {
        return brandCode;
    }

    /**
     * Sets the value of brandCode.
     *
     * @param brandCode the new value
     */
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    /**
     * Gets imgSrc
     *
     * @return imgSrc
     **/
    public String getImgSrc() {
        return imgSrc;
    }

    /**
     * Sets the value of imgSrc.
     *
     * @param imgSrc the new value
     */
    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    /**
     * Gets dateLastUpdate
     *
     * @return dateLastUpdate
     **/
    public String getDateLastUpdate() {
        return dateLastUpdate;
    }

    /**
     * Sets the value of dateLastUpdate.
     *
     * @param dateLastUpdate the new value
     */
    public void setDateLastUpdate(String dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }

    /**
     * Gets shortDescription
     *
     * @return shortDescription
     **/
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the value of shortDescription.
     *
     * @param shortDescription the new value
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Gets description
     *
     * @return description
     **/
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of description.
     *
     * @param description the new value
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
