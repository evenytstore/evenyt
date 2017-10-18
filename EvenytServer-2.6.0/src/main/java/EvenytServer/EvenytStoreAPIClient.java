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

package EvenytServer;

import java.util.*;

import EvenytServer.model.AllBrandForms;
import EvenytServer.model.AllBrands;
import EvenytServer.model.Bundle;
import EvenytServer.model.AllBundles;
import EvenytServer.model.AllCategories;
import EvenytServer.model.Empty;
import EvenytServer.model.Customer;
import EvenytServer.model.AllProducts;
import EvenytServer.model.AllProductsXSizes;
import EvenytServer.model.Sale;
import EvenytServer.model.AllSales;
import EvenytServer.model.AllSizes;
import EvenytServer.model.AllSubcategories;
import EvenytServer.model.AllTopProducts;


@com.amazonaws.mobileconnectors.apigateway.annotation.Service(endpoint = "https://3t94t4v6m2.execute-api.us-east-1.amazonaws.com/prod")
public interface EvenytStoreAPIClient {


    /**
     * A generic invoker to invoke any API Gateway endpoint.
     * @param request
     * @return ApiResponse
     */
    com.amazonaws.mobileconnectors.apigateway.ApiResponse execute(com.amazonaws.mobileconnectors.apigateway.ApiRequest request);
    
    /**
     * 
     * 
     * @return AllBrandForms
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/brandForms", method = "GET")
    AllBrandForms brandFormsGet();
    
    /**
     * 
     * 
     * @return AllBrands
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/brands", method = "GET")
    AllBrands brandsGet();
    
    /**
     * 
     * 
     * @param body 
     * @return Bundle
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/bundles", method = "POST")
    Bundle bundlesPost(
            Bundle body);
    
    /**
     * 
     * 
     * @param idBundle 
     * @return Bundle
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/bundles/idbundle/{idBundle}", method = "GET")
    Bundle bundlesIdbundleIdBundleGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "idBundle", location = "path")
            String idBundle);
    
    /**
     * 
     * 
     * @param idCustomer 
     * @return AllBundles
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/bundles/idcustomer/{idCustomer}", method = "GET")
    AllBundles bundlesIdcustomerIdCustomerGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "idCustomer", location = "path")
            String idCustomer);
    
    /**
     * 
     * 
     * @return AllCategories
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/categories", method = "GET")
    AllCategories categoriesGet();
    
    /**
     * 
     * 
     * @param body 
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/customers", method = "POST")
    Empty customersPost(
            Customer body);
    
    /**
     * 
     * 
     * @param body 
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/customers", method = "PATCH")
    Empty customersPatch(
            Customer body);
    
    /**
     * 
     * 
     * @param idCustomer 
     * @return Customer
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/customers/{idCustomer}", method = "GET")
    Customer customersIdCustomerGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "idCustomer", location = "path")
            String idCustomer);
    
    /**
     * 
     * 
     * @return AllProducts
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/products", method = "GET")
    AllProducts productsGet();
    
    /**
     * 
     * 
     * @return AllProducts
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/productsLastUpdate", method = "GET")
    AllProducts productsLastUpdateGet();
    
    /**
     * 
     * 
     * @return AllProductsXSizes
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/productsXsizes", method = "GET")
    AllProductsXSizes productsXsizesGet();
    
    /**
     * 
     * 
     * @param body 
     * @return Sale
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/sales", method = "POST")
    Sale salesPost(
            Sale body);
    
    /**
     * 
     * 
     * @param body 
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/sales", method = "PATCH")
    Empty salesPatch(
            Sale body);
    
    /**
     * 
     * 
     * @param idCustomer 
     * @return AllSales
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/sales/{idCustomer}", method = "GET")
    AllSales salesIdCustomerGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "idCustomer", location = "path")
            String idCustomer);
    
    /**
     * 
     * 
     * @return AllSizes
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/sizes", method = "GET")
    AllSizes sizesGet();
    
    /**
     * 
     * 
     * @return AllSubcategories
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/subcategories", method = "GET")
    AllSubcategories subcategoriesGet();
    
    /**
     * 
     * 
     * @return AllTopProducts
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/topproducts", method = "GET")
    AllTopProducts topproductsGet();
    
}

