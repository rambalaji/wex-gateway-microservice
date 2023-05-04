package com.wexinc.wexgatewaymicroservice.route;


import com.wexinc.wexgatewaymicroservice.model.OutIntRequest;
import com.wexinc.wexgatewaymicroservice.service.WEXService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sound.midi.SysexMessage;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class IncomingEventListener  extends RouteBuilder {

  //This incoming listener that reads the message from CTRL-M
  //and routes it to the controller which takes care of calling the
  //downstream services.
    @Autowired
    WEXService service;

    @Autowired
    Environment environment;

    // Port via annotation
    @Value("${server.port}")
    int aPort;
    OutIntRequest incomingRequest;



    /**
     * This router receives the messages and routes to the controller endpoint - from there it will call
     * the downstream microservices
     * @throws Exception
     */

    @Override
    public void configure() throws Exception {
        incomingRequest = new OutIntRequest();
        Integer port = new Integer(environment.getProperty("server.port"));

        final AtomicReference<String> uniqueIdentifier = new AtomicReference<>("CorrelationId");

        restConfiguration().bindingMode(RestBindingMode.json).
                host(InetAddress.getLocalHost().getHostAddress()).port(port);
        //Listens the ActiveMQ and routes it to the service to process it.

        from("activemq:ActiveMQ.test.triggerRequestQueue")
                //.setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        JmsMessage message = (JmsMessage) exchange.getIn();
                        uniqueIdentifier.set(message.getJmsMessage().getJMSCorrelationID());
                        //uniqueIdentifier = (String)exchange.getIn().getHeader(Exchange.CORRELATION_ID);
                        System.out.println("Unique Identfier"+uniqueIdentifier.get());
                        exchange.setProperty("uid",uniqueIdentifier.get());
                        exchange.getIn().setHeader(Exchange.CORRELATION_ID, uniqueIdentifier.get());

                    }
                })

                .toD("rest:post:/wexgateway/invoke?CorrelationID=${exchangeProperty.uid}");
        System.out.println("Finished sending message");



    }
}
