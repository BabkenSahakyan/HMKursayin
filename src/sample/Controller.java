package sample;

import javafx.scene.Group;

public class Controller{
    private Group root;
    private final int countOfTests;
    Controller(Group root, int countOfTests){
        this.root = root;
        this.countOfTests = countOfTests;
    }

    public void run(){
        functionality.service.system.System s =
                new functionality.service.system.System(8, 110);
        s.launch(countOfTests, root);
    }
}
