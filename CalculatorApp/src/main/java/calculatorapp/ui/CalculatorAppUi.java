
package calculatorapp.ui;

import calculatorapp.dao.MathDatabase;
import calculatorapp.logic.CalculatorService;
import calculatorapp.logic.ExpressionMemory;
import calculatorapp.logic.InputParser;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import javafx.application.Application;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author jpssilve
 */
public class CalculatorAppUi extends Application {
    private ArrayList<Button> numbers;
    private Button plus;
    private Button minus;
    private Button multiply;
    private Button divide;
    private Button exponent;
    private Button percent;
    private Button modulo;
    private Button delete;
    private Button clear;
    private Button dot;
    private Button equalsSign;
    private Button answer;
    private Button leftBracket;
    private Button rightBracket;
    private Button absValue;
    private ArrayList<Button> mainGridButtons;
    private ArrayList<Button> auxGridButtons;
    
    private Button setMemoryLimit;
    private Button clearMemory;
    private Label currentMemLimit;
    private Label setMemoryLabel;   
    private Slider memLimitSlider;
    
    private Label databaseLabel;
    private Button saveExpression;
    private Button saveAllExpressions;
    private Button getAllSavedExpressions;
    private Button searchExpression;
    private Button deleteExpression;
    private Button deleteAllExpressions;
    
    private Button closeButton;
    private Button yesButton;
    private Button noButton;

    private CalculatorService calc;
    private InputParser inputParser;
    private ExpressionMemory exprMem;
    private int width;
    private int height;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(CalculatorAppUi.class);
    }
    
    @Override
    public void init() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        
        String databaseAddress = properties.getProperty("mathDatabase");
        MathDatabase  math = new MathDatabase("jdbc:sqlite:" + databaseAddress);
        
        try (Connection conn = math.getConnection()) {
            math.initDatabase();
            System.out.println("Database initialization success");
        } catch (SQLException e) {
            System.out.println("Database initialization failure " + e.getMessage());
        }
        
        calc = new CalculatorService();
        inputParser = new InputParser(calc);
        exprMem = new ExpressionMemory(inputParser, 10);
    }

    @Override
    public void start(Stage stage) throws Exception {
        width = 500;
        height = 500;
        
        createAllButtons();
        int divider = 7;
        setButtonSize(mainGridButtons,  divider);
        setButtonSize(auxGridButtons, divider);
        
        GridPane mainGrid = new GridPane();
        mainGrid.addRow(0, numbers.get(7), numbers.get(8), numbers.get(9), delete, clear);
        mainGrid.addRow(1, numbers.get(4), numbers.get(5), numbers.get(6), multiply, divide);
        mainGrid.addRow(2, numbers.get(1), numbers.get(2), numbers.get(3), plus, minus);
        mainGrid.addRow(3, numbers.get(0), dot, percent, answer, equalsSign);

        GridPane auxGrid = new GridPane();
        auxGrid.addColumn(0, exponent, modulo);
        auxGrid.addColumn(1, leftBracket, rightBracket);
        auxGrid.addColumn(2, absValue);
        
        int h2 = 30;
        Label inputLabel = new Label("Input: ");
        inputLabel.setPrefHeight(h2);
        Label exprLabel = new Label("Expression:");
        exprLabel.setPrefHeight(h2);
        Label resLabel = new Label("Result: ");
        resLabel.setPrefHeight(h2);
        Label instrLabel = new Label("Instructions: ");
        instrLabel.setPrefHeight(h2);

        VBox vBoxLeft = new VBox();
        vBoxLeft.getChildren().add(0, inputLabel);
        vBoxLeft.getChildren().add(1, exprLabel);
        vBoxLeft.getChildren().add(2, resLabel);
        vBoxLeft.getChildren().add(3, instrLabel);
        vBoxLeft.getChildren().add(4, auxGrid);
        
        TextField input = new TextField();
        input.setPrefHeight(h2);
        input.setEditable(false);
        TextField formula = new TextField();
        formula.setPrefHeight(h2);
        formula.setEditable(false);
        TextField result = new TextField();
        result.setPrefHeight(h2);
        result.setEditable(false);
        TextField instruction = new TextField();
        instruction.setPrefHeight(h2);
        instruction.setEditable(false);
        
        VBox vBoxCenter = new VBox();
        vBoxCenter.setPrefSize(width, height);
        vBoxCenter.getChildren().add(0, input);
        vBoxCenter.getChildren().add(1, formula);
        vBoxCenter.getChildren().add(2, result);
        vBoxCenter.getChildren().add(3, instruction);
        vBoxCenter.getChildren().add(4, mainGrid);
        
        Label recentExpressions = new Label("Recently used");
        recentExpressions.setPrefHeight(h2);

        ListView<String> memoryList = new ListView<>();
        ListProperty<String> listProperty = new SimpleListProperty<>();
        memoryList.itemsProperty().bind(listProperty);
        listProperty.set(FXCollections.observableArrayList(exprMem.getMemExpressionsArrayList()));
      
        VBox vBoxRight = new VBox();
        vBoxRight.getChildren().add(0, recentExpressions);
        vBoxRight.getChildren().add(1, memoryList);
        
        createSetMemoryLimitSlider();
        VBox vBoxRightest = new VBox();
        vBoxRightest.getChildren().add(0, currentMemLimit);
        vBoxRightest.getChildren().add(1, setMemoryLabel);
        vBoxRightest.getChildren().add(2, memLimitSlider);
        vBoxRightest.getChildren().add(3, setMemoryLimit);
        vBoxRightest.getChildren().add(4, clearMemory);
        
        vBoxRightest.getChildren().add(5, databaseLabel);
        vBoxRightest.getChildren().add(6, saveExpression);
        vBoxRightest.getChildren().add(7, saveAllExpressions);
        vBoxRightest.getChildren().add(8, getAllSavedExpressions);
        vBoxRightest.getChildren().add(9, searchExpression);
        vBoxRightest.getChildren().add(10, deleteExpression);
        vBoxRightest.getChildren().add(11, deleteAllExpressions);
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll(vBoxLeft, vBoxCenter, vBoxRight, vBoxRightest);
        
        numbers.get(0).setOnMouseClicked((event) -> input.setText(input.getText() + "0"));
        numbers.get(1).setOnMouseClicked((event) -> input.setText(input.getText() + "1"));
        numbers.get(2).setOnMouseClicked((event) -> input.setText(input.getText() + "2"));
        numbers.get(3).setOnMouseClicked((event) -> input.setText(input.getText() + "3"));
        numbers.get(4).setOnMouseClicked((event) -> input.setText(input.getText() + "4"));
        numbers.get(5).setOnMouseClicked((event) -> input.setText(input.getText() + "5"));
        numbers.get(6).setOnMouseClicked((event) -> input.setText(input.getText() + "6"));
        numbers.get(7).setOnMouseClicked((event) -> input.setText(input.getText() + "7"));
        numbers.get(8).setOnMouseClicked((event) -> input.setText(input.getText() + "8"));
        numbers.get(9).setOnMouseClicked((event) -> input.setText(input.getText() + "9"));
        
        multiply.setOnMouseClicked((event) -> input.setText(input.getText() + "*"));
        divide.setOnMouseClicked((event) -> input.setText(input.getText() + "/"));
        plus.setOnMouseClicked((event) -> input.setText(input.getText() + "+"));
        minus.setOnMouseClicked((event) -> input.setText(input.getText() + "-"));
        exponent.setOnMouseClicked((event) -> input.setText(input.getText() + "^"));

//        percent.setOnMouseClicked((event) -> input.setText(input.getText() + "%"));
//        modulo.setOnMouseClicked((event) -> input.setText(input.getText() + "mod"));
        
        delete.setOnMouseClicked((event) -> {
            String s = input.getText();
            if (s.length() != 0) {input.setText(input.getText().substring(0, input.getText().length()-1));}
        });
        clear.setOnMouseClicked((event) -> {
            input.setText("");
            formula.setText("");
            result.setText("");
            instruction.setText("");
        });
        dot.setOnMouseClicked((event) -> input.setText((input.getText() + ".")));
        equalsSign.setOnMouseClicked((event) -> {
            Double res = inputParser.expressionEvaluation(input.getText());
            if (res.equals(Double.NaN)) {
                instruction.setText("Is not a valid expression, press delete to fix the expression and try again");
            } else if (Double.isInfinite(res)) {
                instruction.setText("The expression is too long");
            } else {
                formula.setText(input.getText());
                exprMem.addToMemory(input.getText());
                listProperty.set(FXCollections.observableArrayList(exprMem.getMemExpressionsArrayList()));
                input.setText("");
                result.setText("" + res);
                instruction.setText("");
            }
        });
        answer.setOnMouseClicked((event) ->  input.setText((input.getText() + result.getText())));
        leftBracket.setOnMouseClicked((event) ->  input.setText((input.getText() + "(")));
        rightBracket.setOnMouseClicked((event) ->  input.setText((input.getText() + ")")));
        
        setMemoryLimit.setOnMouseClicked((event) -> {
            exprMem.setMemoryLimit((int) memLimitSlider.getValue());
            currentMemLimit.setText("The memory limit is: "  + exprMem.getMemoryLimit());
            listProperty.set(FXCollections.observableArrayList(exprMem.getMemExpressionsArrayList()));
        });
        clearMemory.setOnMouseClicked((event) -> {
            exprMem.clearMemory();
            listProperty.set(FXCollections.observableArrayList(exprMem.getMemExpressionsArrayList()));
        });

        Scene scene = new Scene(hbox);
        stage.setTitle("CalculatorApp");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        System.out.println("Closing the CalculatorApp...");
    }

    private void createSetMemoryLimitSlider() {
        currentMemLimit = new Label("The memory limit is: "  + exprMem.getMemoryLimit());
        setMemoryLabel = new Label("Set memory limit using the slider");
        memLimitSlider = new Slider();
        memLimitSlider.setMin(0);
        memLimitSlider.setMax(25);
        memLimitSlider.setValue(10);
        memLimitSlider.setShowTickLabels(true);
        memLimitSlider.setShowTickMarks(true);
        memLimitSlider.setSnapToTicks(true);
        memLimitSlider.setMajorTickUnit(1);
//        memLimitSlider.setMinorTickCount(1);
//        memLimitSlider.setBlockIncrement(1);
        
//        memLimitSlider.valueProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> ov,
//                Number old_val, Number new_val) {
//            }
//        });
    }
    
    private void createAllButtons() {
        mainGridButtons = new ArrayList<>();
        auxGridButtons = new ArrayList<>();
        createNumberButtons();
        createMathOperatorButtons();
        createOtherButtons();
        createMemoryANdDataBaseButtons();
        createProgramFunctionalityButtons();
    }
    
    private void setButtonSize(ArrayList<Button> buttons, int divider) {
        buttons.forEach(b -> b.setPrefSize(width / divider, height / divider));
    }
    
    private void createNumberButtons() {
        numbers = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            numbers.add(new Button("" + i));
        }
        mainGridButtons.addAll(numbers);
    }
    
    private void createMathOperatorButtons() {
        plus = new Button("+");
        minus = new Button("-");
        multiply = new Button("*");
        divide = new Button("/");
        exponent = new Button("^");
        percent = new Button("%");
        modulo = new Button("mod");
        
        mainGridButtons.add(plus);
        mainGridButtons.add(minus);
        mainGridButtons.add(multiply);
        mainGridButtons.add(divide);
        mainGridButtons.add(percent);

        auxGridButtons.add(modulo);
        auxGridButtons.add(exponent);
    }
    
    private void createOtherButtons() {
        delete = new Button("Delete");
        clear = new Button("Clear");
        dot = new Button(".");
        equalsSign = new Button("=");
        answer = new Button("Ans");
        leftBracket = new Button("(");
        rightBracket = new Button(")");
        absValue = new Button("abs");

        mainGridButtons.add(delete);
        mainGridButtons.add(clear);
        mainGridButtons.add(dot);
        mainGridButtons.add(equalsSign);
        mainGridButtons.add(answer);

        auxGridButtons.add(leftBracket);
        auxGridButtons.add(rightBracket);
        auxGridButtons.add(absValue);
    }
    
    private void createMemoryANdDataBaseButtons() {
        setMemoryLimit = new Button("Set memory limit");
        clearMemory = new Button("Clear memory");
        
        databaseLabel = new Label("Expressions in the database:");
        databaseLabel.setPrefHeight(30);
        saveExpression = new Button("Save the expression");
        saveAllExpressions = new Button("Save all expressions in memory");
        getAllSavedExpressions = new Button("Retrieve all saved expressions");
        searchExpression = new Button("Search for an expression");
        deleteExpression = new Button("Delete the saved expression");
        deleteAllExpressions = new Button("Delete all saved expressions");
    }
    
    private void createProgramFunctionalityButtons() {
        closeButton = new Button("Close the application");
        yesButton = new Button("Yes");
        noButton = new Button("No");
    }
}
