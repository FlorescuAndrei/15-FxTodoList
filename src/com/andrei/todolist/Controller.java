package com.andrei.todolist;

import com.andrei.todolist.datamodel.TodoData;
import com.andrei.todolist.datamodel.TodoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Optional;


public class Controller {

    @FXML
    private BorderPane mainBorderPain;

    @FXML
    private ListView<TodoItem> todoItemListView;

    @FXML
    TextArea itemDetailsTextArea;

    @FXML
    private Label deadlineLabel;

    @FXML
    private ContextMenu listContextMenu;


    public void initialize(){
        // use only once to populate the list
//        TodoItem item = new TodoItem("Doctor's Appointment", "See Dr. Smith at 123 Main Street.", LocalDate.of(2022, 06, 22));
//        TodoData.getInstance().addTodoItem(item);

        //bind data
        todoItemListView.setItems(TodoData.getInstance().getTodoItems());



        //show item details and deadLine
        todoItemListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observableValue, TodoItem oldValue, TodoItem newValue) {
                if(newValue != null){
                    TodoItem item = todoItemListView.getSelectionModel().getSelectedItem();
                    itemDetailsTextArea.setText(item.getDetails());
                    deadlineLabel.setText(item.getDeadLine().toString());
                }
            }
        });
        //show detail from start
        todoItemListView.getSelectionModel().selectFirst();

        // We need a cell factory on the List View to work on specific cell: show item description, use a context menu, etc
        todoItemListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                ListCell<TodoItem> cell = new ListCell<>(){

                   // display description in the list view
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                        }else {
                            setText((item.getDescription()));
                        }
                    }
                };
                //add context menu - Delete, in cell factory
                cell.emptyProperty().addListener((obs, wasEmpty,isNowEmpty) ->{
                    if(isNowEmpty){
                        cell.setContextMenu(null);
                    }else {
                        cell.setContextMenu(listContextMenu);
                    }
                });

                return cell;
            }
        });

        //Delete item. Context menu - Delete
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TodoItem item = todoItemListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });
        listContextMenu.getItems().add(deleteMenuItem);

    }

    // Add a new Item to the list (handle New button in menu)
    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPain.getScene().getWindow());
        dialog.setTitle("Add New Todo Item");
        dialog.setHeaderText("Use this dialog to create a new todo item");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            DialogController dialogController = fxmlLoader.getController();
            TodoItem newItem = dialogController.processResult();
            todoItemListView.getSelectionModel().select(newItem);
            System.out.println("Ok pressed");
        }else{
            System.out.println("Cancel pressed");
        }

    }

    //handle exit button in menu
    @FXML
    public void handleExit(){
        Platform.exit();
    }

    //helper method
    public void deleteItem(TodoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure ?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK)){
            TodoData.getInstance().deleteTodoItem(item);
        }
    }


}
