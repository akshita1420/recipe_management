# Project: manageRecipe
manageRecipe is a Spring Boot application for managing recipes. 
It provides REST APIs to search, filter, and view recipes loaded from a JSON data source into a PostgreSQL database. 
The project uses Spring Data JPA, the Specification pattern for dynamic queries, and Thymeleaf for the frontend.

## Source Code Structure (`src/main/java/com.recipe.manageRecipe`)

```
com.recipe.manageRecipe
├─ config
│   └─ RecipeDataLoader.java      -> Loads JSON data into DB on startup
├─ controller
│   └─ RecipeController.java      -> REST endpoints for recipes
├─ dto
│   └─ SearchCriteria.java        -> Data transfer object for search parameters
├─ entity
│   └─ Recipe.java                -> JPA entity for recipe table
├─ repository
│   └─ RecipeRepository.java      -> JPA repository + specification executor
├─ service
│   └─ RecipeService.java         -> Business logic: load JSON, search, findAll
├─ specification
│   └─ RecipeSpecification.java   -> Dynamic JPA query builder for search
└─ ManageRecipeApplication.java   -> Main Spring Boot app class
```

The backend is structured using Spring Boot, JPA, and the Specification pattern.  
Recipe loading, search, pagination, and all API logic are implemented in this structure.

---

## Resources (`src/main/resources`)

```
static/                -> CSS/JS for frontend
    ├─ css/
    │   └─ style.css
    └─ js/
        └─ recipe.js
templates/             -> Thymeleaf HTML pages
    ├─ layout/
    │   └─ main.html   -> Base layout
    └─ recipes/
        └─ list.html   -> Main recipe page
application.properties -> Spring Boot configuration
US_recipes_null.json   -> JSON data file
schema.sql             -> Database schema
```

- `static/` contains CSS and JS files (`style.css` and `recipe.js`)
- `templates/` contains Thymeleaf templates:
    - `layout/main.html` (Base layout)
    - `recipes/list.html` (Main recipe page)
- `US_recipes_null.json` is the source JSON loaded by `RecipeDataLoader`
- `schema.sql` contains the SQL schema for database setup

---

##  Database Setup

1. Create a PostgreSQL database (e.g., `recipesdb`).
2. Run the schema script to create the required tables:
    ```bash
    psql -U <username> -d recipesdb -f schema.sql
    ```
3. Update database credentials in `src/main/resources/application.properties`:
    ```
    spring.datasource.url=jdbc:postgresql://localhost:5432/recipesdb
    spring.datasource.username=<username>
    spring.datasource.password=<password>
    spring.jpa.hibernate.ddl-auto=update
    ```

---

## Data Import

Place `US_recipes_null.json` in the `src/main/resources/` directory.  
The application parses and loads this file into the database automatically on startup.

---

##  Running the Application

1. Build the project:
    ```bash
    mvn clean install
    ```
2. Start the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```
3. Access the web UI at [http://localhost:8080/recipes](http://localhost:8080/recipes)

---

## API Testing

Use the following endpoints to test the API:

### Get All Recipes (Paginated)
```
GET http://localhost:8080/api/recipes?page=1&size=15
```
**Sample Output:**
```json
{
    "total": 160569,
    "data": [
        {
            "id": 56839,
            "cuisine": "Potato Pancakes",
            "title": "Apple-Potato Latkes",
            "rating": null,
            "prep_time": 15,
            "cook_time": 5,
            "total_time": 20,
            "description": "Golden potato latkes sweetened with fresh apple and maple syrup make a delicious side dish and are a great excuse to use your spiralizer.",
            "nutrients": {
                "calories": "164 kcal",
                "fatContent": "8 g",
                "fiberContent": "2 g",
                "sugarContent": "5 g",
                "sodiumContent": "241 mg",
                "proteinContent": "3 g",
                "cholesterolContent": "47 mg",
                "carbohydrateContent": "20 g",
                "saturatedFatContent": "2 g",
                "unsaturatedFatContent": "0 g"
            },
            "serves": "4 servings"
        }
        // ...more recipes
    ]
}
```

### Search Recipes by Title
```
GET http://localhost:8080/api/recipes/search?title=pizza
```
**Sample Output:**
```json
{
    "total": 836,
    "data": [
        {
            "id": 106538,
            "cuisine": "Breakfast Pizza",
            "title": "Breakfast Pizza with Scrambled Eggs",
            "rating": null,
            "prep_time": 10,
            "cook_time": 25,
            "total_time": 40,
            "description": "Start your day off deliciously with this savory breakfast pizza topped with scrambled eggs, bacon, peppers, onion, and Italian cheese.",
            "nutrients": {
                "calories": "442 kcal",
                "fatContent": "24 g",
                "fiberContent": "1 g",
                "sugarContent": "5 g",
                "sodiumContent": "1123 mg",
                "proteinContent": "25 g",
                "cholesterolContent": "232 mg",
                "carbohydrateContent": "31 g",
                "saturatedFatContent": "10 g",
                "unsaturatedFatContent": "0 g"
            },
            "serves": "8 servings"
        }
        // ...more recipes
    ]
}
```

### Search Recipes by Minimum Rating
```
GET http://localhost:8080/api/recipes/search?rating=4
```
**Sample Output:**
```json
{
    "total": 139061,
    "data": [
        {
            "id": 60792,
            "cuisine": "Southern Drinks",
            "title": "Frozen Derby Mint Juleps",
            "rating": 5.0,
            "prep_time": 5,
            "cook_time": null,
            "total_time": 5,
            "description": "Perfect for Derby Day or any day spent lounging in the sun, these frozen mint juleps are quick and easy to make and refreshingly delicious.",
            "nutrients": {
                "calories": "145 kcal",
                "fiberContent": "0 g",
                "sugarContent": "1 g",
                "sodiumContent": "9 mg",
                "proteinContent": "0 g",
                "carbohydrateContent": "11 g",
                "unsaturatedFatContent": "0 g"
            },
            "serves": "4 servings"
        }
        // ...more recipes
    ]
}
```

### Search Recipes by Cuisine
```
GET http://localhost:8080/api/recipes/search?cuisine=Italian
```
**Sample Output:**
```json
{
    "total": 0,
    "data": [],
    "size": 10,
    "page": 0
}
```

### Search Recipes by Multiple Criteria
```
GET http://localhost:8080/api/recipes/search?title=pizza&cuisine=Italian&rating=4
```
**Sample Output:**
```json
{
    "total": 0,
    "data": [],
    "size": 10,
    "page": 0
}
```

### Get Recipe Details by ID
```
GET http://localhost:8080/api/recipes/1
```
**Sample Error Output:**
```json
{
    "timestamp": "2025-10-07T12:07:56.922+00:00",
    "status": 404,
    "error": "Not Found",
    "path": "/api/recipes/1"
}
```

### Get All Recipes Without Pagination
```
GET http://localhost:8080/api/recipes
```
**Sample Output:**  
Returns all recipes in the same format as paginated output.

---
