package pl.masterthesis.security.model;

import java.util.List;

public record ApiKeyModel(String value, List<String> roles) {

}
