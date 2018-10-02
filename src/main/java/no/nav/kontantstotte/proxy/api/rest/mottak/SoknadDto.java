package no.nav.kontantstotte.proxy.api.rest.mottak;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.Instant;

import java.time.LocalDateTime;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SoknadDto {

    @JsonProperty
    private final byte[] pdf;

    @JsonProperty
    private final String fnr;

    @JsonIgnore
    private final LocalDateTime innsendingTimestamp;

    @JsonCreator
    public SoknadDto(
            @JsonProperty("fnr") String fnr,
            @JsonProperty("pdf") byte[] pdf) {
        this.pdf = pdf;
        this.fnr = fnr;
        this.innsendingTimestamp = null;
    }

    byte[] getPdf() {
        return pdf;
    }

    String getFnr() {
        return fnr;
    }

    LocalDateTime getInnsendingTimestamp() {
        return innsendingTimestamp;
    }
}
