package no.nav.kontantstotte.proxy.service.ws.person;

import no.nav.tjeneste.virksomhet.person.v3.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Informasjonsbehov;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.NorskIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonRequest;

public class RequestUtils {

    static final String OFFISIELL_ADRESSE = "OFFISIELL ADRESSE";
    static final String FNR = "FNR";
    static final String DNR = "DNR";
    static final String MOR = "MORA";
    static final String BARN = "BARN";

    static HentPersonRequest request(String fnr, Informasjonsbehov... behov) {
        HentPersonRequest req = new HentPersonRequest();
        infoBehov(req, behov);
        req.setAktoer(aktor(fnr));
        return req;

    }

    private static void infoBehov(HentPersonRequest req, Informasjonsbehov... behov) {
        for (Informasjonsbehov b : behov) {
            req.getInformasjonsbehov().add(b);
        }
    }

    private static Aktoer aktor(String fnr) {
        PersonIdent aktor = new PersonIdent();
        aktor.setIdent(norskIdent(fnr));
        return aktor;
    }

    private static NorskIdent norskIdent(String fnr) {
        NorskIdent id = new NorskIdent();
        id.setIdent(fnr);
        return id;
    }

    static boolean isFnr(NorskIdent id) {
        return id.getType().getValue().equals(FNR);
    }
}

