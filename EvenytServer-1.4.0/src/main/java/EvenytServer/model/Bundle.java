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


public class Bundle {
    @com.google.gson.annotations.SerializedName("idBundle")
    private Integer idBundle = null;
    @com.google.gson.annotations.SerializedName("name")
    private String name = null;
    @com.google.gson.annotations.SerializedName("frequencyDays")
    private Integer frequencyDays = null;
    @com.google.gson.annotations.SerializedName("description")
    private String description = null;
    @com.google.gson.annotations.SerializedName("lastOrdered")
    private String lastOrdered = null;
    @com.google.gson.annotations.SerializedName("nextDelivery")
    private String nextDelivery = null;
    @com.google.gson.annotations.SerializedName("preferredHour")
    private String preferredHour = null;
    @com.google.gson.annotations.SerializedName("Customer_idCustomer")
    private String customerIdCustomer = null;

    /**
     * Gets idBundle
     *
     * @return idBundle
     **/
    public Integer getIdBundle() {
        return idBundle;
    }

    /**
     * Sets the value of idBundle.
     *
     * @param idBundle the new value
     */
    public void setIdBundle(Integer idBundle) {
        this.idBundle = idBundle;
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
     * Gets frequencyDays
     *
     * @return frequencyDays
     **/
    public Integer getFrequencyDays() {
        return frequencyDays;
    }

    /**
     * Sets the value of frequencyDays.
     *
     * @param frequencyDays the new value
     */
    public void setFrequencyDays(Integer frequencyDays) {
        this.frequencyDays = frequencyDays;
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

    /**
     * Gets lastOrdered
     *
     * @return lastOrdered
     **/
    public String getLastOrdered() {
        return lastOrdered;
    }

    /**
     * Sets the value of lastOrdered.
     *
     * @param lastOrdered the new value
     */
    public void setLastOrdered(String lastOrdered) {
        this.lastOrdered = lastOrdered;
    }

    /**
     * Gets nextDelivery
     *
     * @return nextDelivery
     **/
    public String getNextDelivery() {
        return nextDelivery;
    }

    /**
     * Sets the value of nextDelivery.
     *
     * @param nextDelivery the new value
     */
    public void setNextDelivery(String nextDelivery) {
        this.nextDelivery = nextDelivery;
    }

    /**
     * Gets preferredHour
     *
     * @return preferredHour
     **/
    public String getPreferredHour() {
        return preferredHour;
    }

    /**
     * Sets the value of preferredHour.
     *
     * @param preferredHour the new value
     */
    public void setPreferredHour(String preferredHour) {
        this.preferredHour = preferredHour;
    }

    /**
     * Gets customerIdCustomer
     *
     * @return customerIdCustomer
     **/
    public String getCustomerIdCustomer() {
        return customerIdCustomer;
    }

    /**
     * Sets the value of customerIdCustomer.
     *
     * @param customerIdCustomer the new value
     */
    public void setCustomerIdCustomer(String customerIdCustomer) {
        this.customerIdCustomer = customerIdCustomer;
    }

}
