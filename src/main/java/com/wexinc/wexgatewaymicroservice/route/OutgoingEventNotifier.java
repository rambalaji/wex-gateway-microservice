package com.wexinc.wexgatewaymicroservice.route;

import com.wexinc.wexgatewaymicroservice.model.OutIntRequest;
import com.wexinc.wexgatewaymicroservice.model.ProcessResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class OutgoingEventNotifier extends RouteBuilder {

    //This outgoing notifier that notifies the CTRL-M with the result.
    ProcessResponse response;
    @Override
    public void configure() throws Exception {


        JacksonDataFormat jsonDataFormat = new JacksonDataFormat(ProcessResponse.class);

        //Notifies the ctrl m with the response from the client microservices.
        from("direct:notify")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        ProcessResponse response = (ProcessResponse) exchange.getIn().getBody();
                        exchange.setProperty("uid",response.getUniqueIdentifier());
                        exchange.getIn().setHeader("JMSCorrelationId",response.getUniqueIdentifier());
                        exchange.setProperty("JMSCorrelationId",response.getUniqueIdentifier());
                        //exchange.getOut().setHeader(Exchange.CORRELATION_ID,response.getUniqueIdentifier());
                        }
                })

                .marshal(jsonDataFormat)
                .setHeader(Exchange.CORRELATION_ID,simple("${exchangeProperty.uid}"))
                .setHeader("JMSCorrelationID", simple("${exchangeProperty.uid}"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("activemq:ActiveMQ.test.CTRLMResponseQueue");
        System.out.println("Finished sending message");
    }
}
