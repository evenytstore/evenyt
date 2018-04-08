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

public class Promotions {
    @com.google.gson.annotations.SerializedName("code")
    private String code = null;
    @com.google.gson.annotations.SerializedName("percentage")
    private BigDecimal percentage = null;
    @com.google.gson.annotations.SerializedName("status")
    private Integer status = null;
    @com.google.gson.annotations.SerializedName("amount")
    private BigDecimal amount = null;

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
     * Gets percentage
     *
     * @return percentage
     **/
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * Sets the value of percentage.
     *
     * @param percentage the new value
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
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
     * Gets amount
     *
     * @return amount
     **/
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of amount.
     *
     * @param amount the new value
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
