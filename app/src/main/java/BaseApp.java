import android.app.Application;

import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.model.Recipe;

public class BaseApp extends Application {

//    public FavoritesDatabase getDatabase(){
//        return FavoritesDatabase.getInstance(this);
//    }

    public RecipeRepository getRepository(){
        return RecipeRepository.getInstance();
    }
}
