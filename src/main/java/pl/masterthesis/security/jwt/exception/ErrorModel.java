package pl.masterthesis.security.jwt.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class ErrorModel {

    @Builder.Default
    private final Date timestamp = new Date();
    private int status;
    private String title;
    private String detail;
    private String path;
    private Set<FieldError> errors;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @EqualsAndHashCode
    public static class FieldError {

        private String field;
        private String message;

        public FieldError(String message) {
            this.message = message;
        }

    }

}
