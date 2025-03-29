package source.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import source.restapi.model.Recipe;
import source.restapi.repository.RecipeRepository;

import java.util.*;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    // POST /recipes - Create a recipe
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRecipe(@RequestBody Recipe recipeRequest) {
        Map<String, Object> response = new HashMap<>();

        // Check if all required fields are present
        if (recipeRequest.getTitle() == null || recipeRequest.getMaking_time() == null ||
                recipeRequest.getServes() == null || recipeRequest.getIngredients() == null ||
                recipeRequest.getCost() == null) {

            response.put("message", "Recipe creation failed!");
            response.put("required", "title, making_time, serves, ingredients, cost");
            return ResponseEntity.ok(response);
        }

        Recipe savedRecipe = recipeRepository.save(recipeRequest);

        List<Map<String, Object>> recipeList = new ArrayList<>();
        Map<String, Object> recipeMap = new HashMap<>();
        recipeMap.put("id", savedRecipe.getId());
        recipeMap.put("title", savedRecipe.getTitle());
        recipeMap.put("making_time", savedRecipe.getMaking_time());
        recipeMap.put("serves", savedRecipe.getServes());
        recipeMap.put("ingredients", savedRecipe.getIngredients());
        recipeMap.put("cost", String.valueOf(savedRecipe.getCost()));
        recipeMap.put("created_at", savedRecipe.getCreatedAtFormatted());
        recipeMap.put("updated_at", savedRecipe.getUpdatedAtFormatted());
        recipeList.add(recipeMap);

        response.put("message", "Recipe successfully created!");
        response.put("recipe", recipeList);

        return ResponseEntity.ok(response);
    }

    // GET /recipes - Get all recipes
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        List<Map<String, Object>> recipesList = new ArrayList<>();

        for (Recipe recipe : recipes) {
            Map<String, Object> recipeMap = new HashMap<>();
            recipeMap.put("id", recipe.getId());
            recipeMap.put("title", recipe.getTitle());
            recipeMap.put("making_time", recipe.getMaking_time());
            recipeMap.put("serves", recipe.getServes());
            recipeMap.put("ingredients", recipe.getIngredients());
            recipeMap.put("cost", String.valueOf(recipe.getCost()));
            recipesList.add(recipeMap);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("recipes", recipesList);

        return ResponseEntity.ok(response);
    }

    // GET /recipes/{id} - Get a recipe by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRecipeById(@PathVariable String id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            Map<String, Object> recipeMap = new HashMap<>();
            recipeMap.put("id", recipe.getId());
            recipeMap.put("title", recipe.getTitle());
            recipeMap.put("making_time", recipe.getMaking_time());
            recipeMap.put("serves", recipe.getServes());
            recipeMap.put("ingredients", recipe.getIngredients());
            recipeMap.put("cost", String.valueOf(recipe.getCost()));

            List<Map<String, Object>> recipeList = new ArrayList<>();
            recipeList.add(recipeMap);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Recipe details by id");
            response.put("recipe", recipeList);

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No recipe found");
            return ResponseEntity.ok(response);
        }
    }

    // PATCH /recipes/{id} - Update a recipe
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRecipe(@PathVariable String id, @RequestBody Recipe recipeRequest) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();

            if (recipeRequest.getTitle() != null) {
                recipe.setTitle(recipeRequest.getTitle());
            }
            if (recipeRequest.getMaking_time() != null) {
                recipe.setMaking_time(recipeRequest.getMaking_time());
            }
            if (recipeRequest.getServes() != null) {
                recipe.setServes(recipeRequest.getServes());
            }
            if (recipeRequest.getIngredients() != null) {
                recipe.setIngredients(recipeRequest.getIngredients());
            }
            if (recipeRequest.getCost() != null) {
                recipe.setCost(recipeRequest.getCost());
            }

            Recipe updatedRecipe = recipeRepository.save(recipe);

            Map<String, Object> recipeMap = new HashMap<>();
            recipeMap.put("id", updatedRecipe.getId());
            recipeMap.put("title", updatedRecipe.getTitle());
            recipeMap.put("making_time", updatedRecipe.getMaking_time());
            recipeMap.put("serves", updatedRecipe.getServes());
            recipeMap.put("ingredients", updatedRecipe.getIngredients());
            recipeMap.put("cost", String.valueOf(updatedRecipe.getCost()));

            List<Map<String, Object>> recipeList = new ArrayList<>();
            recipeList.add(recipeMap);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Recipe successfully updated!");
            response.put("recipe", recipeList);

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No recipe found");
            return ResponseEntity.ok(response);
        }
    }

    // DELETE /recipes/{id} - Delete a recipe
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRecipe(@PathVariable String id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (recipeOptional.isPresent()) {
            recipeRepository.deleteById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Recipe successfully removed!");

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No recipe found");
            return ResponseEntity.ok(response);
        }
    }
}
