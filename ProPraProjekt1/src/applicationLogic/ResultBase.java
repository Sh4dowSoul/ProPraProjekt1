package applicationLogic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author Niklas Schnettler
 *
 */
public abstract class ResultBase {

	@XStreamAlias("ESNummer")
	private int id;
	@XStreamOmitField
	private LocalDate date;
	@XStreamOmitField
	private String niceDate;
	
	
	public ResultBase(int id, LocalDate date) {
		this.id = id;
		this.date = date;
		this.niceDate = date.format(DateTimeFormatter.ofPattern("dd.MM.YYYY")); 
	}
	
	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public String getNiceDate() {
		return niceDate;
	}
}
