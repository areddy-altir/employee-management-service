package co.altir.example;

import co.altir.dbmanagement.dataaccess.openapidsl.OpenAPIConfig;
import co.altir.dbmanagement.dataaccess.openapidsl.OpenAPIConfig.DtoGeneratorConfig.ModelSplitDepth;
import co.altir.dbmanagement.dataaccess.openapidsl.OpenAPIGenerator;

/**
 * OpenAPI spec generator
 *
 * <p>Can be executed as simple main class with one application argument - path to api-hub project
 * directory. Another option to run via maven:
 *
 * <pre>
 *   mvn clean install
 * </pre>
 */
public class ApiHubApplication {

  public static void main(String[] args) {
    String baseDir = OpenAPIGenerator.resolveBasePath(args);

    // Common generator configuration
    OpenAPIConfig cfg =
        new OpenAPIConfig()
            .baseDir(baseDir) // api hub project path
            .useErrorSchemaClass(ErrorResponse.class) // response object in case of error
            // in endpoint
            .responseWrapper(
                b ->
                    b.useWrapper(
                            (type, isArray) ->
                                true) // use wrapper for particular data model and response type
                        // (single object or list of objects)
                        //            .wrapperClass(ApiResponseBase.class) //common wrapper class
                        // for all success responses
                        .wrapperFieldName(
                            (type, isArray) -> "data") // additional field name, that will be added
                        .wrapperClassResolver(
                            (type, isArray) -> isArray ? ArrayResponseBase.class : null)
                // to wrapper class and will contain response object
                )
            .dtoGeneratorConfig(b -> b.modelSplitDepth(ModelSplitDepth.SINGLE_DTO));

    OpenAPIGenerator.initialize(cfg);

    // Generate APIs for all classes, located in classpath, that extend OpenAPIGenerator class
    OpenAPIGenerator.generateForAllInClasspath();
  }
}
