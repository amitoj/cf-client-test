package com.example;

import lombok.extern.slf4j.Slf4j;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.UaaClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CfClientTest.class)
public class CfClientTest {

    private ConnectionContext connectionContext;
    private UaaClient uaaClient;
    private DopplerClient dopplerClient;
    private CloudFoundryClient cloudFoundryClient;
    private TokenProvider tokenProvider;

    @Before
    public void setup() {
        this.tokenProvider = PasswordGrantTokenProvider.builder()
                .password("user")
                .username("pass")
                .build();
        this.connectionContext = DefaultConnectionContext.builder()
                .apiHost("api.local.pcfdev.io")
                .skipSslValidation(true)
                .build();
        this.uaaClient = ReactorUaaClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
        this.dopplerClient = ReactorDopplerClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
        this.cloudFoundryClient = ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

    @Test
    public void testGetOrganizations() throws InterruptedException {
        log.info("testing getting orgs");
        DefaultCloudFoundryOperations operations = DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .dopplerClient(dopplerClient)
                .uaaClient(uaaClient)
                .organization("pcfdev-org")
                .space("pcfdev-space")
                .build();

        CountDownLatch latch = new CountDownLatch(1);

        operations.organizations().list().subscribe(System.out::println, t -> {
            t.printStackTrace();
            latch.countDown();
        }, latch::countDown);

        latch.await();
    }


}
