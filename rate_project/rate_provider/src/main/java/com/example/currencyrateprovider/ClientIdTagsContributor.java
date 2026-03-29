package com.example.currencyrateprovider;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsContributor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ClientIdTagsContributor implements WebMvcTagsContributor {

    @Override
    public Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response,
                                   Object handler, HandlerMethod handlerMethod) {
        String client = request.getHeader("X-Client-Id");
        if (client == null) {
            client = "unknown";
        }
        return Tags.of("client", client);
    }
}
