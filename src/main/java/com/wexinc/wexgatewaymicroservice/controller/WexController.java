package com.wexinc.wexgatewaymicroservice.controller;

import com.wexinc.wexgatewaymicroservice.model.InterfaceFiles;
import com.wexinc.wexgatewaymicroservice.model.OutIntRequest;
import com.wexinc.wexgatewaymicroservice.model.ProcessResponse;
import com.wexinc.wexgatewaymicroservice.service.WEXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
@RestController
@RequestMapping("/wexgateway")
public class WexController {

    @Autowired
    private WEXService service;

    private OutIntRequest incomingRequest;

    /**
     * This method invoke the service method to process the incoming messag from CTRLM
     * @param process
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/invoke", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProcessResponse invoke(@RequestBody OutIntRequest process, @RequestParam("CorrelationID") String correlationID) throws Exception {


        String interfaceID = process.getInterfaceID();
        String clientMid = process.getClientId();
        System.out.println("Correlation ID================ "+correlationID);
       /*if (process.getUniqueIdentifier() != null ) {
            correlationID = process.getUniqueIdentifier();
        } else {
            correlationID =  new Integer).toString());
        }*/

        OutIntRequest request = new OutIntRequest();
        request.setClientId(clientMid);
        request.setInterfaceID(interfaceID);
        request.setUniqueIdentifier(correlationID);


        service.invoke(request);

        ProcessResponse response = new ProcessResponse();

        response.setResult("Success");
        return response;

    }

    /**
     * This endpoint receives the callback after a long-running process, it can be either from IFCS or
     * Client specific microservices.
     * @param process
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/callback", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void receiveCallback(@RequestBody ProcessResponse notification) throws Exception {

        InterfaceFiles files = notification.getInputFiles().get(0);
        System.out.println("Received Notification"+files.getFileName());
        OutIntRequest request = new OutIntRequest();
        request.setInterfaceID(notification.getInterfaceID());
        request.setClientId(notification.getClientId());
        request.setUniqueIdentifier(notification.getUniqueIdentifier());
        request.setInputFiles(notification.getInputFiles());
        service.transform(request);


    }

    /**
     * This endpoint is to notify the result to the Ctrl m through the response queue.
     * @param notification
     * @throws Exception
     */
    @PostMapping(path = "/notify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void notify(@RequestBody ProcessResponse notification) throws Exception {

        InterfaceFiles files = notification.getInputFiles().get(0);
        System.out.println("Client Microservice finished the process"+files.getFileName());

        service.notify(notification);

    }

    }
