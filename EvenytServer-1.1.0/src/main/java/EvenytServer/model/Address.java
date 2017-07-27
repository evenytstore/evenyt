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

public class Address {
    @com.google.gson.annotations.SerializedName("addressName")
    private String addressName = null;
    @com.google.gson.annotations.SerializedName("addressNumber")
    private String addressNumber = null;
    @com.google.gson.annotations.SerializedName("latitude")
    private BigDecimal latitude = null;
    @com.google.gson.annotations.SerializedName("longitude")
    private BigDecimal longitude = null;
    @com.google.gson.annotations.SerializedName("district")
    private String district = null;
    @com.google.gson.annotations.SerializedName("city")
    private String city = null;

    /**
     * Gets addressName
     *
     * @return addressName
     **/
    public String getAddressName() {
        return addressName;
    }

    /**
     * Sets the value of addressName.
     *
     * @param addressName the new value
     */
    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    /**
     * Gets addressNumber
     *
     * @return addressNumber
     **/
    public String getAddressNumber() {
        return addressNumber;
    }

    /**
     * Sets the value of addressNumber.
     *
     * @param addressNumber the new value
     */
    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    /**
     * Gets latitude
     *
     * @return latitude
     **/
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of latitude.
     *
     * @param latitude the new value
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude
     *
     * @return longitude
     **/
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of longitude.
     *
     * @param longitude the new value
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets district
     *
     * @return district
     **/
    public String getDistrict() {
        return district;
    }

    /**
     * Sets the value of district.
     *
     * @param district the new value
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Gets city
     *
     * @return city
     **/
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of city.
     *
     * @param city the new value
     */
    public void setCity(String city) {
        this.city = city;
    }

}
