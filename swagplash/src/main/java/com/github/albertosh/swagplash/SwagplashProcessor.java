package com.github.albertosh.swagplash;

import com.github.albertosh.swagplash.annotations.*;
import com.github.albertosh.swagplash.model.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.*;

@SupportedAnnotationTypes({
        "com.github.albertosh.swagplash.annotations.SwaggerDefinition"
})
public class SwagplashProcessor
        extends AbstractProcessor {

    private final SwagplashSaver swagplashSaver;
    private SPSwaggerDefinition swagger;

    public SwagplashProcessor() {
        swagplashSaver = new SwagplashSaver();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public final void warning(String message, Element e) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.WARNING,
                message,
                e);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Utils.init(processingEnv);
        processSwagger(annotations, roundEnv);
        processApiModel(annotations, roundEnv);
        processSecureDefinitions(annotations, roundEnv);
        processApi(annotations, roundEnv);

        if (roundEnv.processingOver()) {
            swagplashSaver.save(swagger, processingEnv);
        }

        return true;
    }

    private void processSwagger(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        int i = 0;
        Element definitionUsed = null;
        for (Element elemDefinition : roundEnv.getElementsAnnotatedWith(SwaggerDefinition.class)) {
            if (i > 0) {
                warning("Found more than one @SwaggerDefinition. The one found at " + definitionUsed.getSimpleName().toString() + " will be used", elemDefinition);
            } else {
                i++;
                definitionUsed = elemDefinition;
                SwaggerDefinition def = elemDefinition.getAnnotation(SwaggerDefinition.class);
                swagger = new SPSwaggerDefinition(def);
            }
        }
    }


    private void processApiModel(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element apiModelElement : roundEnv.getElementsAnnotatedWith(ApiModel.class)) {
            ApiModel apiModel = apiModelElement.getAnnotation(ApiModel.class);
            if (!apiModel.hidden()) {
                SPApiModel spApiModel = new SPApiModel(apiModel, (TypeElement) apiModelElement);
                processApiModelProperties((TypeElement) apiModelElement, spApiModel);
                swagger.addDefinition(spApiModel);
            }
        }
    }

    private void processApiModelProperties(TypeElement apiModelElement, SPApiModel spApiModel) {
        for (Element field : apiModelElement.getEnclosedElements()) {
            if (field.getKind().isField()) {
                addField((VariableElement) field, spApiModel);
            } else if (field.getKind() == ElementKind.METHOD) {
                addPropertyMethod((ExecutableElement) field, spApiModel);
            }
        }
    }

    private void addPropertyMethod(ExecutableElement field, SPApiModel spApiModel) {
        ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
        if (apiModelProperty != null) {
            if (apiModelProperty.hidden())
                return;
            SPApiModelProperty spApiModelProperty = new SPApiModelProperty(apiModelProperty, field);
            spApiModel.addProperty(spApiModelProperty);
        }
    }

    private void addField(VariableElement field, SPApiModel spApiModel) {
        ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
        if (apiModelProperty != null) {
            if (apiModelProperty.hidden())
                return;
            SPApiModelProperty spApiModelProperty = new SPApiModelProperty(apiModelProperty, field);
            spApiModel.addProperty(spApiModelProperty);
        }
    }


    private void processSecureDefinitions(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element secureDefinitionElement : roundEnv.getElementsAnnotatedWith(SecureDefinition.class)) {
            SecureDefinition secureDefinition = secureDefinitionElement.getAnnotation(SecureDefinition.class);
            addSecureDefinition(secureDefinition, secureDefinitionElement);
        }
    }

    private void addSecureDefinition(SecureDefinition secureDefinition, Element secureDefinitionElement) {
        SPSecureDefinition spSecureDefinition = new SPSecureDefinition(secureDefinition, (TypeElement) secureDefinitionElement);
        swagger.addSecureDefinition(spSecureDefinition);
    }



    private void processApi(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element apiElement : roundEnv.getElementsAnnotatedWith(Api.class)) {
            Api api = apiElement.getAnnotation(Api.class);
            if (!api.hidden()) {
                addApi(api, apiElement);
            }
        }
    }

    private void addApi(Api api, Element apiElement) {
        SPApi spApi = new SPApi(api, swagger, (TypeElement) apiElement);
        for (Element method : apiElement.getEnclosedElements()) {
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            if ((apiOperation != null) && (!apiOperation.hidden())) {
                addApiOperation(spApi, apiOperation, (ExecutableElement) method);
            }
        }
    }

    private void addApiOperation(SPApi api, ApiOperation apiOperation, ExecutableElement method) {
        SPApiOperation spApiOperation = new SPApiOperation(apiOperation, api, method);
        swagger.addApiOperation(spApiOperation, method);
    }
}
