package de.felunka.autoBackup;

public class Lang {
	public String TENSECONDANNOUNCE;
	public String COUNTINGBEFORE;
	public String COUNTINGAFTER;
	public String SAVINGANNOUNCE;
	public String SAVINGDONEANNOUNCE;
	
	public Lang() {
		this.TENSECONDANNOUNCE = "Server is saving world in 10 seconds. Please excuse lag for a second.";
		this.COUNTINGBEFORE = "Server is saving world in";
		this.COUNTINGAFTER = " seconds...";
		this.SAVINGANNOUNCE = "Server is saving world. Please excuse lag for a second...";
		this.SAVINGDONEANNOUNCE = "Save complete!";
	}
	
	public String getTenSecond() {
		return "[AutoBackup] " + this.TENSECONDANNOUNCE;
	}
	
	public String getCount(int sec) {
		return "[AutoBackup] " + this.COUNTINGBEFORE + " " + sec + " " + this.COUNTINGAFTER;
	}
	
	public String getSavingAnn() {
		return "[AutoBackup] " + this.SAVINGANNOUNCE;
	}
	
	public String getSavingDone() {
		return "[AutoBackup] " + this.SAVINGDONEANNOUNCE;
	}

}
