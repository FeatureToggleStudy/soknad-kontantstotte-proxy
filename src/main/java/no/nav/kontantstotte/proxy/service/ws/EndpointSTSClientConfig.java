package no.nav.kontantstotte.proxy.service.ws;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.trust.STSClient;
import org.springframework.stereotype.Component;

@Component
public class EndpointSTSClientConfig {

    private static final String POLICY_PATH = "classpath:policy/";
    private static final String STS_REQUEST_SAML_POLICY = POLICY_PATH + "requestSamlPolicy.xml";

    private STSClient stsClient;

    public EndpointSTSClientConfig(STSClient stsClient) {
        this.stsClient = stsClient;
    }

    public <T> T configureRequestSamlToken(T port) {
        Client client = ClientProxy.getClient(port);
        // do not have onbehalfof token so cache token in endpoint
        configureEndpointWithPolicyForSTS(stsClient, client, STS_REQUEST_SAML_POLICY, true);
        return port;
    }

    public <T> T configureRequestSamlTokenOnBehalfOfOidc(T port, OnBehalfOfOutInterceptor onBehalfOfOutInterceptor) {
        Client client = ClientProxy.getClient(port);
        client.getOutInterceptors().add(onBehalfOfOutInterceptor);
        // want to cache the token with the OnBehalfOfToken, not per proxy
        configureEndpointWithPolicyForSTS(stsClient, client, STS_REQUEST_SAML_POLICY, false);
        return port;
    }

    private void configureEndpointWithPolicyForSTS(STSClient stsClient, Client client, String policyReference,
                                                   boolean cacheTokenInEndpoint) {
        client.getRequestContext().put(org.apache.cxf.rt.security.SecurityConstants.STS_CLIENT, stsClient);
        client.getRequestContext().put(org.apache.cxf.rt.security.SecurityConstants.CACHE_ISSUED_TOKEN_IN_ENDPOINT,
                cacheTokenInEndpoint);
        setEndpointPolicyReference(client, policyReference);
    }

    private void setEndpointPolicyReference(Client client, String uri) {
        Policy policy = resolvePolicyReference(client, uri);
        setClientEndpointPolicy(client, policy);
    }

    private Policy resolvePolicyReference(Client client, String uri) {
        PolicyBuilder policyBuilder = client.getBus().getExtension(PolicyBuilder.class);
        ReferenceResolver resolver = new RemoteReferenceResolver("", policyBuilder);
        return resolver.resolveReference(uri);
    }

    private void setClientEndpointPolicy(Client client, Policy policy) {
        Endpoint endpoint = client.getEndpoint();
        EndpointInfo endpointInfo = endpoint.getEndpointInfo();

        PolicyEngine policyEngine = client.getBus().getExtension(PolicyEngine.class);
        SoapMessage message = new SoapMessage(Soap12.getInstance());
        EndpointPolicy endpointPolicy = policyEngine.getClientEndpointPolicy(endpointInfo, null, message);
        policyEngine.setClientEndpointPolicy(endpointInfo, endpointPolicy.updatePolicy(policy, message));
    }
}

