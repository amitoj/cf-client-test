package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CfClientTestMain.class)
public class CfClientTest {

    @Autowired
    private CloudFoundryService cloudFoundryService;

    @Test
    public void testGetOrganizations() throws InterruptedException {
        cloudFoundryService.listOrgs();
    }

    @Test
    public void testGetAppUsageEvents() throws InterruptedException {
        cloudFoundryService.listUsageEvents();
    }

    @Test
    public void getGetApps() throws InterruptedException {
        cloudFoundryService.listApps();
    }


}
