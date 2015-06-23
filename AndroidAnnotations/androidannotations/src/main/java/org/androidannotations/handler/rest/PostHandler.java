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
package org.androidannotations.handler.rest;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import org.androidannotations.annotations.rest.Field;
import org.androidannotations.annotations.rest.Part;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.handler.AnnotationHandler;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.handler.HasParameterHandlers;
import org.androidannotations.holder.GeneratedClassHolder;
import org.androidannotations.holder.RestHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JVar;

public class PostHandler extends RestMethodHandler implements HasParameterHandlers<RestHolder> {

	private FieldHandler fieldHandler;
	private PartHandler partHandler;

	public PostHandler(ProcessingEnvironment processingEnvironment) {
		super(Post.class, processingEnvironment);
		fieldHandler = new FieldHandler(processingEnvironment);
		partHandler = new PartHandler(processingEnvironment);
	}

	@Override
	public Iterable<AnnotationHandler<? extends GeneratedClassHolder>> getParameterHandlers() {
		return Arrays.<AnnotationHandler<? extends GeneratedClassHolder>> asList(fieldHandler, partHandler);
	}

	@Override
	public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
		super.validate(element, validatedElements, valid);

		validatorHelper.doesNotReturnPrimitive((ExecutableElement) element, valid);

		restAnnotationHelper.urlVariableNamesExistInParametersAndHasOnlyOneEntityParameterOrOneOrMorePostParameter((ExecutableElement) element, valid);

		restAnnotationHelper.doesNotMixPartAndFieldAnnotations((ExecutableElement) element, valid);
	}

	@Override
	protected String getUrlSuffix(Element element) {
		Post annotation = element.getAnnotation(Post.class);
		return annotation.value();
	}

	@Override
	protected JExpression getRequestEntity(ExecutableElement element, RestHolder holder, JBlock methodBody, SortedMap<String, JVar> params) {
		JVar httpHeaders = restAnnotationHelper.declareHttpHeaders(element, holder, methodBody);
		JVar entitySentToServer = restAnnotationHelper.getEntitySentToServer(element, params);

		if (entitySentToServer == null) {
			Map<String, String> postParameters = restAnnotationHelper.extractPostParameters(element);

			if (postParameters != null) {
				JClass hashMapClass = holder.classes().LINKED_MULTI_VALUE_MAP.narrow(String.class, Object.class);
				entitySentToServer = methodBody.decl(hashMapClass, "postParameters", JExpr._new(hashMapClass));

				for (Entry<String, String> postParameter : postParameters.entrySet()) {
					methodBody.add(entitySentToServer.invoke("add").arg(JExpr.lit(postParameter.getKey())).arg(params.get(postParameter.getValue())));
				}
			}
		}

		return restAnnotationHelper.declareHttpEntity(processHolder, methodBody, entitySentToServer, httpHeaders);
	}

	private abstract class AbstractPostParamHandler extends BaseAnnotationHandler<GeneratedClassHolder> {

		public AbstractPostParamHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
			super(targetClass, processingEnvironment);
		}

		@Override
		public void process(Element element, GeneratedClassHolder holder) throws Exception {
			// Don't do anything here.
		}

		@Override
		protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
			validatorHelper.enclosingElementHasAnnotation(Post.class, element, validatedElements, valid);

			validatorHelper.doesNotHavePathParamAnnotation(element, validatedElements, valid);

			validatorHelper.doesNotHaveQueryParamAnnotation(element, validatedElements, valid);

			validatorHelper.restInterfaceHasFormConverter(element, validatedElements, valid);
		}
	}

	public class FieldHandler extends AbstractPostParamHandler {

		public FieldHandler(ProcessingEnvironment processingEnvironment) {
			super(Field.class, processingEnvironment);
		}

		@Override
		protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
			super.validate(element, validatedElements, valid);

			validatorHelper.doesNotHavePartAnnotation(element, validatedElements, valid);
		}
	}

	public class PartHandler extends AbstractPostParamHandler {

		public PartHandler(ProcessingEnvironment processingEnvironment) {
			super(Part.class, processingEnvironment);
		}

		@Override
		protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
			super.validate(element, validatedElements, valid);

			validatorHelper.doesNotHaveFieldAnnotation(element, validatedElements, valid);
		}
	}
}
