package no.nav.kontantstotte.proxy.api.rest.mottak;

import no.nav.kontantstotte.proxy.dokumentinnsending.domain.SoknadSender;
import no.nav.security.oidc.api.Unprotected;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("soknad")
public class SoknadMottakResource {

    private final SoknadSender soknadSender;

    private final SoknadConverter converter = new SoknadConverter();

    @Inject
    public SoknadMottakResource(SoknadSender soknadSender) {

        this.soknadSender = soknadSender;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Unprotected // TODO Add protection at some point!
    public Response mottaSoknad(SoknadDto soknadDto) {
        soknadSender.send(converter.toSoknad(soknadDto));
        return Response.ok().build();
    }

}
