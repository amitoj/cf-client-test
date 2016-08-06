package com.example;

import org.cloudfoundry.client.v2.applicationusageevents.ListApplicationUsageEventsRequest;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class CloudFoundryService {

    private DefaultCloudFoundryOperations operations;

    @Autowired
    public CloudFoundryService(DefaultCloudFoundryOperations operations) {
        this.operations = operations;
    }

    public void listOrgs() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        operations.organizations().list().subscribe(
                System.out::println,
                t -> {
                    t.printStackTrace();
                    latch.countDown();
                },
                latch::countDown);

        latch.await();
    }

    public void listApps() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        operations.applications().list().subscribe(
                System.out::println,
                t -> {
                    t.printStackTrace();
                    latch.countDown();
                },
                latch::countDown
        );
        latch.await();
    }

    public void listUsageEvents() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        operations.getCloudFoundryClient().applicationUsageEvents().list(
                ListApplicationUsageEventsRequest.builder().build()
        ).subscribe(
                System.out::println,
                t -> {
                    t.printStackTrace();
                    latch.countDown();
                },
                latch::countDown
        );
        latch.await();
    }

}
