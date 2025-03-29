package source.restapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import source.restapi.model.Recipe;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
}
