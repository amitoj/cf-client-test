import lombok.extern.slf4j.Slf4j;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.applications.ListApplicationsRequest;
import org.cloudfoundry.client.v2.applications.ListApplicationsResponse;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.organizations.OrganizationSummary;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.junit.Test;
import reactor.core.publisher.Mono;

public class CfClientTest {

    @Test
    public void test() {
        ListApplicationsRequest request = ListApplicationsRequest.builder().build();
        Mono<ListApplicationsResponse> responseMono = cloudFoundryClient().applicationsV2().list(request);
    }

    private CloudFoundryClient cloudFoundryClient() {
        return ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext())
                .tokenProvider(tokenProvider())
                .build();

    }

    private ConnectionContext connectionContext() {
        return DefaultConnectionContext.builder()
                .apiHost("api.local.pcfdev.io")
                .build();
    }

    private TokenProvider tokenProvider() {
        return PasswordGrantTokenProvider.builder()
                .password("user")
                .username("pass")
                .build();
    }

    ReactorDopplerClient dopplerClient() {
        return ReactorDopplerClient.builder()
                .connectionContext(connectionContext())
                .tokenProvider(tokenProvider())
                .build();
    }

    ReactorUaaClient uaaClient() {
        return ReactorUaaClient.builder()
                .connectionContext(connectionContext())
                .tokenProvider(tokenProvider())
                .build();
    }

    @Test
    public void testWithOperations() {
        DefaultCloudFoundryOperations operations = DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient())
                .dopplerClient(dopplerClient())
                .uaaClient(uaaClient())
                .organization("pcfdev-org")
                .space("pcfdev-space")
                .build();
        operations.organizations()
                .list()
                .map(OrganizationSummary::getName)
                .subscribe(this::print);
        operations.buildpacks().list().subscribe(System.out::println);
    }

    public void print(String s) {
        System.out.println(s);
    }

}
