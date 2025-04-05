package application;

import java.util.Stack;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static final Stack<Scene> sceneStack = new Stack<>();

    /**
     * Pushes the given scene onto the navigation stack.
     *
     * @param scene The scene to be stored.
     */
    public static void pushScene(Scene scene) {
        sceneStack.push(scene);
    }

    /**
     * Pops and returns the previous scene from the navigation stack.
     *
     * @return The previous Scene, or null if the stack is empty.
     */
    public static Scene popScene() {
        return sceneStack.isEmpty() ? null : sceneStack.pop();
    }

    /**
     * Checks if there is a previous scene available.
     *
     * @return True if there is at least one scene in the stack.
     */
    public static boolean hasPrevious() {
        return !sceneStack.isEmpty();
    }

    /**
     * Navigates back to the previous scene, if one exists.
     *
     * @param primaryStage The primary stage on which to set the previous scene.
     */
    public static void goBack(Stage primaryStage) {
        if (!sceneStack.isEmpty()) {
            primaryStage.setScene(sceneStack.pop());
        }
    }
}
