package s28476.tpo11.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import s28476.tpo11.constraint.PasswordComplexity;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkDTO {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @Size(min = 5, max = 20, message = "{linkDTO.constraint.name.size}")
    private String name;

    @NotBlank(message = "{linkDTO.constraint.targetUrl.notBlank}")
    @Pattern(regexp = "https://.*", message = "{linkDTO.constraint.targetUrl.pattern}")
    @URL(message = "{linkDTO.constraint.targetUrl.url}")
    private String targetUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String redirectUrl;

    @PasswordComplexity
    @Nullable
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int visits;

    @Override
    public String toString() {
        return "LinkDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                ", password='" + password + '\'' +
                ", visits=" + visits +
                '}';
    }
}