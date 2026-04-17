package com.example.currencyrateprovider;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.net.InetAddress;

@Component
public class ZooKeeperRegistration {
    
    @Value("${server.port}")
    private int port;
    
    private CuratorFramework client;
    private ServiceDiscovery<Void> serviceDiscovery;
    private ServiceInstance<Void> instance;
    
    @EventListener(ApplicationReadyEvent.class)
    public void register() throws Exception {
        String host = InetAddress.getLocalHost().getHostAddress();
        String instanceId = "provider-" + System.currentTimeMillis();
        
        client = CuratorFrameworkFactory.newClient(
            "localhost:2181",
            new ExponentialBackoffRetry(1000, 3)
        );
        client.start();
        
        instance = ServiceInstance.<Void>builder()
            .name("currency-rate-service")
            .id(instanceId)
            .address(host)
            .port(port)
            .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
            .build();
        
        serviceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
            .client(client)
            .basePath("/services")
            .thisInstance(instance)
            .build();
        
        serviceDiscovery.start();
        System.out.println("Registered in ZooKeeper with id: " + instanceId);
    }
    
    @PreDestroy
    public void unregister() throws Exception {
        if (serviceDiscovery != null) {
            serviceDiscovery.close();
        }
        if (client != null) {
            client.close();
        }
        System.out.println("Unregistered from ZooKeeper");
    }
}
