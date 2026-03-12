package com.example.rateprinter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateFetchService {
    
    private CuratorFramework client;
    private ServiceDiscovery<Void> serviceDiscovery;
    private ServiceProvider<Void> serviceProvider;
    private AtomicInteger counter = new AtomicInteger(0);
    
    @PostConstruct
    public void init() throws Exception {
        client = CuratorFrameworkFactory.newClient(
            "localhost:2181",
            new ExponentialBackoffRetry(1000, 3)
        );
        client.start();
        
        serviceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
            .client(client)
            .basePath("/services")
            .build();
        serviceDiscovery.start();
        
        serviceProvider = serviceDiscovery.serviceProviderBuilder()
            .serviceName("currency-rate-service")
            .build();
        serviceProvider.start();
    }
    
    public CurrencyRate fetchCurrentRate() {
        try {
            List<ServiceInstance<Void>> instances = serviceProvider.getAllInstances();
            if (instances.isEmpty()) {
                System.out.println("No providers available");
                return null;
            }
            
            int index = Math.abs(counter.getAndIncrement() % instances.size());
            ServiceInstance<Void> instance = instances.get(index);
            
            String url = "http://" + instance.getAddress() + ":" + instance.getPort() + "/api/rates/usdrub";
            
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, CurrencyRate.class);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    @PreDestroy
    public void destroy() throws Exception {
        if (serviceProvider != null) serviceProvider.close();
        if (serviceDiscovery != null) serviceDiscovery.close();
        if (client != null) client.close();
    }
}
