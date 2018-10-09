package com.syo.platform.security.cas;

import java.util.Properties;

import org.jasig.cas.client.ssl.HttpsURLConnectionFactory;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.syo.platform.sysLog.login.LogoutActionHandler;


@Configuration
@EnableConfigurationProperties({CasServerConfig.class, CasServiceConfig.class})
public class SecurityConfiguration {

	@Autowired
    private CasServerConfig casServerConfig;

    @Autowired
    private CasServiceConfig casServiceConfig;
    
    @Bean
    AuthenticationUserDetailsService myUserDetailsService() {
		return new MyUserDetailsService();
	}
    
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(this.casServiceConfig.getHost() + this.casServiceConfig.getLogin());
        serviceProperties.setSendRenew(this.casServiceConfig.getSendRenew());
        
        return serviceProperties;
    }
    
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(AuthenticationManager authenticationManager, ServiceProperties serviceProperties) {
//        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
    	CasAuthenticationFilter casAuthenticationFilter = new MyCasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager);
        casAuthenticationFilter.setServiceProperties(serviceProperties);
        casAuthenticationFilter.setFilterProcessesUrl(this.casServiceConfig.getLogin());
        casAuthenticationFilter.setContinueChainBeforeSuccessfulAuthentication(false);
        casAuthenticationFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
        
        return casAuthenticationFilter;
    }
    
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint(ServiceProperties serviceProperties) {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(this.casServerConfig.getLogin());
        entryPoint.setServiceProperties(serviceProperties);
        
        return entryPoint;
    }
    
    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
    	Cas20ServiceTicketValidator validator = new Cas20ServiceTicketValidator(this.casServerConfig.getHost());
    	validator.setURLConnectionFactory(new HttpsURLConnectionFactory(new com.sdhf.cas.client.CasHostNameVerifier(), new Properties()));
    	validator.setEncoding("utf-8");
        return validator;
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(
            AuthenticationUserDetailsService<CasAssertionAuthenticationToken> userDetailsService,
            ServiceProperties serviceProperties, Cas20ServiceTicketValidator ticketValidator) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setKey("casProvider");
        provider.setServiceProperties(serviceProperties);
        provider.setTicketValidator(ticketValidator);
        provider.setAuthenticationUserDetailsService(userDetailsService);
        

        return provider;
    }

    
    @Bean
	LogoutHandler logoutHandler() {
		return new LogoutActionHandler();
	}
    
    @Bean
    public LogoutFilter logoutFilter() {
        String logoutRedirectPath = this.casServerConfig.getLogout() + "?service=" +
                this.casServiceConfig.getHost();
        LogoutFilter logoutFilter = new LogoutFilter(logoutRedirectPath, logoutHandler(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(this.casServiceConfig.getLogout());
        return logoutFilter;
    }
	
}
