package de.unistuttgart.iste.pe2.api.ToDos;

import org.springframework.stereotype.Service;

/**
 * Service class for predicting categories of ToDo items.
 * Uses a PMML model to predict the appropriate category based on the ToDo title.
 */
@Service
public class PredictService {

    private final TodoModel todoModel;

    /**
     * Constructs a new PredictService and initializes the prediction model.
     */
    public PredictService() {
        this.todoModel = new TodoModel("model.pmml");
    }

    /**
     * Predicts the category for a given ToDo title.
     *
     * @param title The title of the ToDo item
     * @return Predicted category string
     */
    public String predictCategory(String title) {
        return todoModel.predictClass(title);
    }
}


