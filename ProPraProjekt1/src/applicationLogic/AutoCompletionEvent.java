package applicationLogic;

/**
 * @author Niklas Schnettler
 *
 */
public interface AutoCompletionEvent {
	void onAutoCompleteResult (AutocompleteSuggestion result);
}