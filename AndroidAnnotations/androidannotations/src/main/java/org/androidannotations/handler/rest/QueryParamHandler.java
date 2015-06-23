package org.androidannotations.handler.rest;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import org.androidannotations.annotations.rest.QueryParam;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.holder.RestHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

public class QueryParamHandler extends BaseAnnotationHandler<RestHolder> {

	public QueryParamHandler(ProcessingEnvironment processingEnvironment) {
		super(QueryParam.class, processingEnvironment);
	}

	@Override
	protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
		validatorHelper.enclosingElementHasRestAnnotation(element, validatedElements, valid);

		validatorHelper.doesNotHavePathParamAnnotation(element, validatedElements, valid);

		validatorHelper.doesNotHaveFieldAnnotation(element, validatedElements, valid);

		validatorHelper.doesNotHavePartAnnotation(element, validatedElements, valid);
	}

	@Override
	public void process(Element element, RestHolder holder) throws Exception {

	}
}
