/*
 * Copyright 2020 Koushik R <rkoushik.14@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.dropwizard.serviceproxy.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author koushik
 */
public class ProxyConfigTest {

    @Test
    public void testDefaultValuesForConfig(){
        ProxyConfiguration configuration = new ProxyConfiguration();

        Assert.assertEquals(configuration.getConnections(), 10);
        Assert.assertEquals(configuration.getConnectionTimeoutMs(), 10000);
        Assert.assertEquals(configuration.getIdleTimeoutSeconds(), 30);
        Assert.assertEquals(configuration.getOperationTimeoutMs(), 10000);
    }
}
