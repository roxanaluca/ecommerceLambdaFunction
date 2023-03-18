package com.ecommerce.lambda.model;

import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler;
import com.amazonaws.serverless.proxy.jersey.suppliers.AwsProxyServletContextSupplier;
import com.amazonaws.serverless.proxy.jersey.suppliers.AwsProxyServletRequestSupplier;
import com.amazonaws.serverless.proxy.jersey.suppliers.AwsProxyServletResponseSupplier;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.ecommerce.lambda.controller.NotificationController;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EcommerceModel implements RequestStreamHandler {
    private final ResourceConfig jerseyApplication = new ResourceConfig()
            .packages("com.mcamp.resource")
            .packages("com.mcamp.controller")
            .register(new AbstractBinder() {
                @Override
                protected void configure() {

                    bindFactory(AwsProxyServletContextSupplier.class)
                            .to(ServletContext.class).in(RequestScoped.class);
                    bindFactory(AwsProxyServletRequestSupplier.class)
                            .to(HttpServletRequest.class).in(RequestScoped.class);
                    bindFactory(AwsProxyServletResponseSupplier.class)
                            .to(HttpServletResponse.class).in(RequestScoped.class);
                    bind(new NotificationController()).to(NotificationController.class);
                }
            }).register(new Feature() {
                @Override
                public boolean configure(FeatureContext featureContext) {

                    return true;
                }
            })
            .register(JacksonFeature.class);
    private final JerseyLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler =
            JerseyLambdaContainerHandler.getHttpApiV2ProxyHandler(jerseyApplication);
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}
