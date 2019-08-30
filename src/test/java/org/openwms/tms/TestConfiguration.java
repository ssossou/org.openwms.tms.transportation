/*
 * Copyright 2005-2019 the original author or authors.
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
package org.openwms.tms;

import org.openwms.core.SpringProfiles;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * A TestConfiguration.
 *
 * @author Heiko Scherrer
 */
@Profile({SpringProfiles.ASYNCHRONOUS_PROFILE})
@Configuration
class TestConfiguration {

    // ------ Common Resources -------

    @Bean
    TopicExchange commonTuExchange() {
        return new TopicExchange("common.tu", true, false);
    }
    @Bean
    TopicExchange commonTuCommandsExchange() {
        return new TopicExchange("common.tu.commands", true, false);
    }


    @Bean
    TestStateAcceptor testStateAcceptor(AmqpTemplate amqpTemplate) {
        return new TestStateAcceptor(amqpTemplate);
    }

    @Bean
    Queue tmsRequestsTestQueue(@Value("test-tms-requests-queue") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    Binding tmsRequestsTestBinding(TopicExchange tmsRequestsExchange, Queue tmsRequestsTestQueue) {
        return BindingBuilder
                .bind(tmsRequestsTestQueue)
                .to(tmsRequestsExchange)
                .with("request.state.change");
    }

}