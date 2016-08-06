package com.example.config;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by linseb on 06/08/16.
 */
@Configuration
@ComponentScan(basePackages = {"com.example"})
public class CloudConfig {

    @Value("${cf.user}")
    private String cfUser;
    @Value("${cf.pass}")
    private String cfPass;
    @Value("${cf.host}")
    private String cfHost;

    @Bean
    public DefaultCloudFoundryOperations cloudFoundryOperations() {
        TokenProvider tokenProvider = PasswordGrantTokenProvider.builder()
                .username(cfUser)
                .password(cfPass)
                .build();
        ConnectionContext connectionContext = DefaultConnectionContext.builder()
                .apiHost(cfHost)
                .skipSslValidation(true)
                .build();
        UaaClient uaaClient = ReactorUaaClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
        DopplerClient dopplerClient = ReactorDopplerClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
        CloudFoundryClient cloudFoundryClient = ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
        return DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .dopplerClient(dopplerClient)
                .uaaClient(uaaClient)
                .organization("pcfdev-org")
                .space("pcfdev-space")
                .build();
    }

}


