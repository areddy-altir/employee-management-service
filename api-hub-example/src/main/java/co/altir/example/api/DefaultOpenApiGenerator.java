package co.altir.example.api;

import co.altir.dbmanagement.dataaccess.openapidsl.OpenAPIConfig;
import co.altir.dbmanagement.dataaccess.openapidsl.OpenAPIGenerator;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public abstract class DefaultOpenApiGenerator extends OpenAPIGenerator {

  private static final List<String> MOCK_SERVER_PATTERNS =
      List.of(
          "http://10.40.1.223:8080/%s",
          "http://localhost:8080/%s", "https://dev.logixflow.info/%s");

  private final ServiceMetadata serviceMetadata;

  protected DefaultOpenApiGenerator(final ServiceMetadata serviceMetadata) {
    super(
        serviceMetadata.openApiConfigOverrides,
        "swagger/%s/api.yaml".formatted(serviceMetadata.name.replace('.', '-')),
        serviceMetadata.name);

    this.serviceMetadata = serviceMetadata;
  }

  @Override
  protected void init() {
    final List<Server> servers =
        Stream.concat(
                MOCK_SERVER_PATTERNS.stream().map(p -> p.formatted(serviceName)),
                Stream.ofNullable(serviceMetadata.additionalServerUrls).flatMap(Collection::stream))
            .map(s -> new Server().url(s))
            .toList();
    api.servers(servers);

    final String apiTitle = "%s API".formatted(StringUtils.capitalize(serviceName));
    info(
        apiTitle,
        apiTitle + " for Logix Flow",
        serviceMetadata.maintainerEmail,
        serviceMetadata.version);
    bearerSecuritySchema();
    scanAllControllersInPackage("co.altir.example.controller." + serviceMetadata.name);
    scanAllModelsInPackage("co.altir.example.model." + serviceMetadata.name);
  }

  public record ServiceMetadata(
      String name,
      String version,
      String maintainerEmail,
      @Nullable List<String> additionalServerUrls,
      @Nullable OpenAPIConfig openApiConfigOverrides) {
    public ServiceMetadata(final String name, final String version, final String maintainerEmail) {
      this(name, version, maintainerEmail, null, null);
    }
  }
}
