package applicationLogic;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
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

public class AutocompleteTextField extends TextField {
	//Possible Suggestions
    private final SortedSet<AutocompleteSuggestion> entries;      
    //Suggestion PopUp
    private ContextMenu entriesPopup;
    
    private AutoCompletionEvent event;


    public AutocompleteTextField() {
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();
      
        setListner();
    }  
    
    public void setAutoCompletionEvent(AutoCompletionEvent event) {
    	this.event = event;
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
                List<AutocompleteSuggestion> filteredEntries = entries.stream().filter(e -> e.getDescription().toLowerCase().startsWith(enteredText.toLowerCase())).collect(Collectors.toList());
                //Check for remaining Suggestions
                if (!filteredEntries.isEmpty()) {
                    //Suggestions found
                    populatePopup(filteredEntries, enteredText);
                    //If popUp is not visible yet, show it
                    if (!entriesPopup.isShowing()) {
                        entriesPopup.show(AutocompleteTextField.this, Side.BOTTOM, 0, 0); //position of popup
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
    private void populatePopup(List<AutocompleteSuggestion> searchResult, String searchReauest) {
        //List of Suggestions
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //Build list as set of labels
        for (int i = 0; i < searchResult.size(); i++) {
          final AutocompleteSuggestion result = searchResult.get(i);
          //Create a label for the text preview and style it
          Label entryLabel = new Label();
          entryLabel.setGraphic(buildTextFlow(result.getDescription(), searchReauest));  
          entryLabel.setPrefHeight(20);
          CustomMenuItem item = new CustomMenuItem(entryLabel, true);
          menuItems.add(item);

          //Suggestion selected Listener
          item.setOnAction(actionEvent -> {
              setText(result.getDescription());
              event.onAutoCompleteResult(result);
              positionCaret(result.getDescription().length());
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
    public SortedSet<AutocompleteSuggestion> getEntries() { 
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