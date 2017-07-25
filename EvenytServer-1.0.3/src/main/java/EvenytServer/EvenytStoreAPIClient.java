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
import EvenytServer.model.AllCategories;
import EvenytServer.model.AllProducts;
import EvenytServer.model.AllSizes;
import EvenytServer.model.AllSubcategories;


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
     * @return AllCategories
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/categories", method = "GET")
    AllCategories categoriesGet();
    
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
    
}

