/**
 * Copyright (C) 2010-2015 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.androidannotations.rest;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.PathParam;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

@Rest(converters = MappingJacksonHttpMessageConverter.class)
public interface ClientWithWrongPathVariables {

	@Get("/duplicates/{v1}")
	void getWithDuplicatePathVariables(@PathParam("v1") int v1, @PathParam("v1") int v2);

	@Get("/missingvariable/{v1}")
	void getWithMissingPathVariable(@PathParam("v1") int v1, @PathParam("v2") int hasMissingVariable);

	@Get("/missingparameter/{v1}")
	void getWithMissingMethodParameter(@PathParam("v1") int v1);

	void missingGetAnnotation(@PathParam("missingGet") int v1);
}