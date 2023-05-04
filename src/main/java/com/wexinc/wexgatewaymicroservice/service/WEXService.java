package com.wexinc.wexgatewaymicroservice.service;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.wexinc.wexgatewaymicroservice.config.ConfigProperties;
import com.wexinc.wexgatewaymicroservice.model.OutIntRequest;
import com.wexinc.wexgatewaymicroservice.model.ProcessResponse;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.datatransfer.StringSelection;
import java.net.InetAddress;
import java.util.List;

@Service
public class WEXService {
    @Autowired
    private CamelContext camelContext;

    @Autowired
    ConsulClient cclient;
    @Autowired
    private ConfigProperties config;
    @Autowired
    RestTemplate template;

    /**
     * This is to invoke the IFCS or Client Speciifc microservices to generate file.
     * @param request
     * @throws Exception
     */
    public void invoke(OutIntRequest request) throws Exception
    {
        //Here after receiving the incoming message, retreive the required values from the config and route it to the router
        System.out.println("Incoming request from CTRL-M "+request.getInterfaceID());
        //String key =  cclient.getKVBinaryValue(request.getInterfaceID()+"."+"ifcsservice").);
        System.out.println("Config from Consul "+config.getIfcsservice());
        Response<GetValue> value = cclient.getKVValue(request.getInterfaceID()+".ifcsservice");
        //System.out.println("Consul"+value.getValue().getValue());

        //Call the rest  end point straight
        String response = template.postForObject("http://wex-ifcs-microservice/embossCard/invokeEmbossCard", request,  String.class) ;



    }

    public void transform(OutIntRequest request) throws Exception
    {
        //Here after receiving the incoming message, retrieve the required values from the config
        //and invoke the clientMicroservice
        System.out.println("File generated for : "+request.getInterfaceID());
        //String key =  cclient.getKVBinaryValue(request.getInterfaceID()+"."+"ifcsservice").);
        //Here some translation will be required from IFCS interface to interface for outgoing.
        System.out.println("Config from Consul "+config.getIfcsservice());


        //call the rest end point straight
        String response = template.postForObject("http://wex-client-microservice/client/transform", request,  String.class) ;



    }

    public void notify(ProcessResponse response) throws Exception
    {
        //Here after receiving the incoming message, retrieve the required values from the config and route it to the router
        System.out.println("Outgoing response to CtrlM "+response.getInterfaceID());


        //Place the notification message in the queue
        camelContext.createProducerTemplate()
                   .sendBodyAndHeader("direct:notify",
                       ExchangePattern.InOnly,response,"JMSCorrelationID",response.getUniqueIdentifier());
        ;

    }


}
