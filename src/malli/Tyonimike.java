package malli;

public class Tyonimike
{
	private String nimi;
	private double tuntipalkka;
	private boolean tyoVartija;
	private boolean tyoMyyja;
	
	static Tyonimike vartija = new Tyonimike("Vartija", 14.0, true, false);
	static Tyonimike myyja = new Tyonimike("Myyjä", 11.0, false, true);

	public Tyonimike(final String nimi, final double tuntipalkka, final boolean vartija, final boolean myyja)
	{
		this.nimi = nimi;
		this.tuntipalkka = tuntipalkka;
		this.tyoVartija = vartija;
		this.tyoMyyja = myyja;
	}
	
	public String toString()
	{
		return nimi;
	}
	
	public double getTuntipalkka()
	{
		return tuntipalkka;
	}
	
	public boolean josVartija()
	{
		return tyoVartija;
	}
	
	public boolean josMyyja()
	{
		return tyoMyyja;
	}
}