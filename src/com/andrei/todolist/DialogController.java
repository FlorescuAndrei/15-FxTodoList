package com.andrei.todolist;

import com.andrei.todolist.datamodel.TodoData;
import com.andrei.todolist.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {

    @FXML
    private TextField descriptionField;

    @FXML
    private TextArea detailArea;

    @FXML
    private DatePicker deadLinePicker;


    public TodoItem processResult(){
        String description = descriptionField.getText().trim();
        String detail = detailArea.getText().trim();
        LocalDate date = deadLinePicker.getValue();

        TodoItem newItem = new TodoItem(description, detail, date);
        TodoData.getInstance().addTodoItem(newItem);

        return newItem;
    }
}
