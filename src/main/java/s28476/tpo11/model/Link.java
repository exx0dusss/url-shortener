package s28476.tpo11.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Link {
    @Id
    private String id;

    private String name;
    private String targetUrl;
    private String redirectUrl;
    private String password;

    private int visits;

    @Override
    public String toString() {
        return "Link{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                ", password='" + password + '\'' +
                ", visits=" + visits +
                '}';
    }
}
