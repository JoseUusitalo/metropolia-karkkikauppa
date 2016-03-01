package malli;

import java.util.ArrayList;

import tuki.MersenneTwisterFast;

/**
 * Ei käytetty.
 */
public class Kaluste
{
	private String tyyppi;
	private double hinta;
	private int viihtyisyys;
	
	public Kaluste(final String tyyppi, final double hinta, final int viihtyisyys)
	{
		this.tyyppi = tyyppi;
		this.hinta = hinta;
		this.viihtyisyys = viihtyisyys;
	}
	
	/**
	 * @return [100,3500] hintaisen kalusteen viihtyvyysarvolla [1,10],
	 */
	public static Kaluste luoKaluste()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();
		
		return new Kaluste(Nimi.luoKalusteenNimi(), satunnaisluku.randInt(100, 3500), satunnaisluku.randInt(9) + 1);
	}
	
	public static ArrayList<Kaluste> luoKalusteVarasto(final int koko)
	{
		ArrayList<Kaluste> tempvarasto = new ArrayList<Kaluste>();
		
		for (int i = 0; i < koko; i++)
		{
			tempvarasto.add(luoKaluste());
		}
		
		return tempvarasto;
	}
	
	public void tulosta()
	{
		System.out.println(tyyppi + ", " + viihtyisyys + ", " + hinta + "€");
	}
}
