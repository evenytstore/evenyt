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

public class Customer {
    @com.google.gson.annotations.SerializedName("idCustomer")
    private String idCustomer = null;
    @com.google.gson.annotations.SerializedName("name")
    private String name = null;
    @com.google.gson.annotations.SerializedName("lastName")
    private String lastName = null;
    @com.google.gson.annotations.SerializedName("email")
    private String email = null;
    @com.google.gson.annotations.SerializedName("phoneNumber")
    private String phoneNumber = null;
    @com.google.gson.annotations.SerializedName("DNI")
    private String DNI = null;
    @com.google.gson.annotations.SerializedName("RUC")
    private String RUC = null;
    @com.google.gson.annotations.SerializedName("birthday")
    private String birthday = null;
    @com.google.gson.annotations.SerializedName("address")
    private Address address = null;

    /**
     * Gets idCustomer
     *
     * @return idCustomer
     **/
    public String getIdCustomer() {
        return idCustomer;
    }

    /**
     * Sets the value of idCustomer.
     *
     * @param idCustomer the new value
     */
    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
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
     * Gets lastName
     *
     * @return lastName
     **/
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of lastName.
     *
     * @param lastName the new value
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets email
     *
     * @return email
     **/
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of email.
     *
     * @param email the new value
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets phoneNumber
     *
     * @return phoneNumber
     **/
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value of phoneNumber.
     *
     * @param phoneNumber the new value
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets DNI
     *
     * @return DNI
     **/
    public String getDNI() {
        return DNI;
    }

    /**
     * Sets the value of DNI.
     *
     * @param DNI the new value
     */
    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    /**
     * Gets RUC
     *
     * @return RUC
     **/
    public String getRUC() {
        return RUC;
    }

    /**
     * Sets the value of RUC.
     *
     * @param RUC the new value
     */
    public void setRUC(String RUC) {
        this.RUC = RUC;
    }

    /**
     * Gets birthday
     *
     * @return birthday
     **/
    public String getBirthday() {
        return birthday;
    }

    /**
     * Sets the value of birthday.
     *
     * @param birthday the new value
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

}
