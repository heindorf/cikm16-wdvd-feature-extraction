package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

// Workaround because some alias lists are formatted like
// {"2":"aliasXY", "5":"aliasZZ"} instead of ["aliasXY", "aliasZZ"]
@JsonDeserialize(using = OldAliasListDeserializer.class)
public class OldAliasList {

}
