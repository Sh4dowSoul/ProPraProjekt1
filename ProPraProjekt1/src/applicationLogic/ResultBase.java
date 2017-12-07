package applicationLogic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class ResultBase {

	@XStreamAlias("ESNummer")
	private int id;
	@XStreamOmitField
	private LocalDate date;
	
	
	public ResultBase(int id, LocalDate date) {
		this.id = id;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public String getNiceDate() {
		return date.format(DateTimeFormatter.ofPattern("dd.MM.YYYY"));
	}
}
