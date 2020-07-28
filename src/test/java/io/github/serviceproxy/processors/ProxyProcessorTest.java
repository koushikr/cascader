package io.github.serviceproxy.processors;

import io.github.serviceproxy.config.ProxyConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author koushik
 */
public class ProxyProcessorTest {

    private static ProxyProcessor proxyProcessor;

    @BeforeClass
    public static void initialize(){
        ProxyConfiguration proxyConfiguration = ProxyConfiguration.builder()
                .host("host")
                .port(80)
                .scheme("http")
                .build();
        proxyProcessor = new ProxyProcessor(proxyConfiguration,null);
    }

    @Test
    public void testHeadRequest(){

    }

    @Test
    public void testGetRequest(){

    }

    @Test
    public void testPutRequest(){

    }

    @Test
    public void testPostRequest(){

    }

    @Test
    public void testDeleteRequest(){

    }

    @Test
    public void testOptionsRequest(){

    }

    @Test
    public void testPatchRequest(){

    }
}
