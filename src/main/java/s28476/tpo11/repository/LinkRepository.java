package s28476.tpo11.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import s28476.tpo11.model.Link;
import java.util.Optional;

@Repository
public interface LinkRepository extends CrudRepository<Link, Integer> {
    Optional<Link> findById(String id);

    Optional<Link> findByName(String name);

}
