package userInterface;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import applicationLogic.DefectAtomic;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class BranchAutocompletionTextField extends TextField {
	//Possible Suggestions
    private final SortedSet<DefectAtomic> entries;      
    //Suggestion PopUp
    private ContextMenu entriesPopup;


    public BranchAutocompletionTextField() {
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();
        
        setListner();
    }  



    /**
     * Suggestion Listener
     */
    private void setListner() {     
        //Add "suggestions" by changing text
        textProperty().addListener((observable, oldValue, newValue) -> {
            String enteredText = getText();
            //Hide Suggestion - No Input
            if (enteredText == null || enteredText.isEmpty()) {
                entriesPopup.hide();
            } else {
                //Filter Suggestions
                List<DefectAtomic> filteredEntries = entries.stream().filter(e -> e.getDefectDescription().toLowerCase().startsWith(enteredText.toLowerCase())).collect(Collectors.toList());
                //Check for remaining Suggestions
                if (!filteredEntries.isEmpty()) {
                    //Suggestions found
                    populatePopup(filteredEntries, enteredText);
                    //If popUp is not visible yet, show it
                    if (!entriesPopup.isShowing()) {
                        entriesPopup.show(BranchAutocompletionTextField.this, Side.BOTTOM, 0, 0); //position of popup
                    } 
                //No Suggestions found - Hide PopUp
                } else {
                    entriesPopup.hide();
                }
            }
        });

        //Hide always by focus-in (optional) and out
        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.hide();
        });
    }             


    /**
    * Populate the entry set with the given search results. Display is limited to 10 entries, for performance.
    * 
    * @param searchResult The set of matching strings.
    */
    private void populatePopup(List<DefectAtomic> searchResult, String searchReauest) {
        //List of Suggestions
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //Build list as set of labels
        for (int i = 0; i < searchResult.size(); i++) {
          final DefectAtomic result = searchResult.get(i);
          //Create a label for the text preview and style it
          Label entryLabel = new Label();
          entryLabel.setGraphic(buildTextFlow(result.getDefectDescription(), searchReauest));  
          entryLabel.setPrefHeight(20);
          CustomMenuItem item = new CustomMenuItem(entryLabel, true);
          menuItems.add(item);

          //Suggestion selected Listener
          item.setOnAction(actionEvent -> {
        	  System.out.println("Selected" + result.getDefectId());
              setText(result.getDefectDescription());
              positionCaret(result.getDefectDescription().length());
              entriesPopup.hide();
          });
        }

        //Refresh
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }


    /**
    * Get the existing set of autocomplete entries.
    * 
    * @return The existing autocomplete entries.
    */
    public SortedSet<DefectAtomic> getEntries() { 
    	return entries; }
    
    
    //Style
    public static TextFlow buildTextFlow(String text, String filter) {        
        int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
        Text textBefore = new Text(text.substring(0, filterIndex));
        Text textAfter = new Text(text.substring(filterIndex + filter.length()));
        Text textFilter = new Text(text.substring(filterIndex,  filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
        textFilter.setFill(Color.BLUE);
        textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));  
        return new TextFlow(textBefore, textFilter, textAfter);
    }    
    
    
}