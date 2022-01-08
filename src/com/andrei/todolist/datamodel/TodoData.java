package com.andrei.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

// Singleton class that will store/load the data to/from a file and will be access from Main and Controller
public class TodoData {

    private static TodoData instance = new TodoData();
    private static ObservableList<TodoItem> todoItems; //same as List from java.util but better optimize for JavaFX

    private static String filename = "TodoListItems.txt"; //will self-generate

    //help to change from LocalDate to String and vice versa to save/load TodoItem item to/from txt file
    private DateTimeFormatter formatter;


    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }


    public static TodoData getInstance() {
        return instance;
    }

    public static ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }


    public void addTodoItem(TodoItem item){
        todoItems.add(item);
    }

    public void deleteTodoItem(TodoItem item){
        todoItems.remove(item);
    }

    public void storeTodoItems() throws IOException{
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try{
            Iterator<TodoItem> iter = todoItems.iterator();
            while (iter.hasNext()){
                TodoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s\t", item.getDescription(), item.getDetails(), item.getDeadLine().format(formatter)));
                bw.newLine();
            }

        }finally {
            if(bw != null){
                bw.close();
            }
        }
    }

    public void loadTodoItems() throws IOException{

        todoItems = FXCollections.observableArrayList(); // initialize
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while ((input = br.readLine()) != null){
                String[] itemPieces = input.split("\t");

                String description = itemPieces[0];
                String details = itemPieces[1];
                String dataString = itemPieces[2];

                LocalDate date = LocalDate.parse(dataString,formatter);

                TodoItem item = new TodoItem(description, details, date);
                todoItems.add(item);
            }

        }finally {
            if(br != null){
                br.close();
            }
        }
    }
}
