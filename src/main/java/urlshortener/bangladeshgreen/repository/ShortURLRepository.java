package urlshortener.bangladeshgreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import urlshortener.bangladeshgreen.domain.Click;
import urlshortener.bangladeshgreen.domain.ShortURL;
import urlshortener.bangladeshgreen.domain.URIDisabled;

import java.net.URI;
import java.util.List;

/*
 * Short URL repository
 */
@Component
public interface ShortURLRepository extends MongoRepository<ShortURL, String> {

	public ShortURL findByHash(String hash);

	@Query("{'target' : ?0}")
	public List<ShortURL> findByTarget(String target);


	@Query("{}")
	public List<ShortURL> list();

	@Query("{'creator' : ?0}")
	public List<ShortURL> findByCreator(String creator);

	@Query("{'target' : ?0, 'creator' : ?1}")
	public List<ShortURL> find(String target, String creator);
}
