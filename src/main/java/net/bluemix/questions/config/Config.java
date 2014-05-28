/*
 * Copyright IBM Corp. 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bluemix.questions.config;

import java.util.List;

import javax.sql.DataSource;

import net.bluemix.questions.xauth.CustomUserDetailsService;
import net.bluemix.questions.xauth.XAuthTokenConfigurer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.cloud.service.relational.DataSourceConfig.ConnectionConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;

import com.twilio.sdk.TwilioRestClient;

public class Config {

  @Configuration
  @Profile("cloud")
  static class CloudConfiguration extends AbstractCloudConfig {
    @Bean
    public DataSource dataSource() {
      return connectionFactory().dataSource("session-questions-sql", new DataSourceConfig(new PoolConfig(4, 4), new ConnectionConfig("")));
    }

    @Bean
    public MailSender mailSender() {
      MailSender sender = connectionFactory().service(MailSender.class);
      return sender;
    }

    @Bean
    public TwilioRestClient twilioRestClient() {
      TwilioRestClient client = connectionFactory().service(TwilioRestClient.class);
      
      return client;
    }

    @Bean
    public AmqpServiceInfo amqpServiceInfo() {
      Cloud cloud = cloud();
      List<ServiceInfo> services = cloud.getServiceInfos();
      for(ServiceInfo service : services) {
        if(service instanceof AmqpServiceInfo) {
          return ((AmqpServiceInfo)service);
        }
      }
      return null;
    }
    
    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
      return connectionFactory().rabbitConnectionFactory();
    }
    
    @Bean
    public AmqpTemplate rabbitTemplate() {
      return new RabbitTemplate(rabbitConnectionFactory());
    }
  }


  @Configuration
  @Profile("default")
  static class LocalConfiguration {
    @Value("${smtp.host}")
    private String smtpHost;
    @Value("${smtp.port}")
    private int smtpPort;
    @Value("${smtp.username}")
    private String smtpUsername;
    @Value("${smtp.password}")
    private String smtpPassword;
    @Value("${twilio.accountId}")
    private String twilioAccountId;
    @Value("${twilio.authToken}")
    private String twilioAuthToken;

    @Bean 
    public MailSender mailSender() {
      JavaMailSenderImpl sender = new JavaMailSenderImpl();
      sender.setHost(smtpHost);
      sender.setPort(smtpPort);
      sender.setUsername(smtpUsername);
      sender.setPassword(smtpPassword);
      return sender;
    }

    @Bean
    public TwilioRestClient twilioRestClient() {
      return new TwilioRestClient(twilioAccountId, twilioAuthToken);
    }
  }

  @EnableWebMvcSecurity
  @EnableWebSecurity(debug = false)
  @Configuration
  @Order
  static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

      http.csrf().disable();
      http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

      String[] restEndpointsToSecure = { "api/admin"};
      for (String endpoint : restEndpointsToSecure) {
        http.authorizeRequests().antMatchers("/" + endpoint + "/**").hasRole(CustomUserDetailsService.ROLE_USER);
      }

      SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> securityConfigurerAdapter = new XAuthTokenConfigurer(userDetailsServiceBean());
      http.apply(securityConfigurerAdapter);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
      authManagerBuilder.userDetailsService(new CustomUserDetailsService());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
      return super.userDetailsServiceBean();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
    }
  }
}