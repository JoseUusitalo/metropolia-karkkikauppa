package malli;

import tuki.Mat;
import tuki.MersenneTwisterFast;

/**
 * Henkilökunta myy kaupan karkkia.
 */
public class Henkilokunta
{
	private Nimi nimi;
	private double palkka;
	private Tyonimike tyonimike;
	private int tyoaika;
	
	public Henkilokunta(final double palkka, final Tyonimike tyonimike, final int tyoaika)
	{
		nimi = Nimi.luoHenkilonNimi();
		this.palkka = palkka;
		this.tyonimike = tyonimike;
		this.tyoaika = tyoaika;
	}
	
	public Henkilokunta(final double palkka, final Tyonimike tyonimike)
	{
		nimi = Nimi.luoHenkilonNimi();
		this.palkka = palkka;
		this.tyonimike = tyonimike;
		tyoaika = 0;
	}
	
	/**
	 * @return satunnaisen työntekijän, 50% mahdollisuus olla myyjä.
	 */
	public static Henkilokunta luoTyontekija()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();
		Tyonimike tyo;
		
		// Vartija tai myyjä, vartijoilla
		if (satunnaisluku.randBoolean(0.5))
			tyo = Tyonimike.vartija;
		else
			tyo = Tyonimike.myyja;
		
		// Tuntipalkka on pohjat + max 3 e
		return new Henkilokunta(Mat.pakotaDesimaalit(2, tyo.getTuntipalkka() + (satunnaisluku.randInt(400) / 100.0)), tyo);
	}
	
	public void tulosta()
	{
		System.out.println(nimi.toString() + ", " + palkka + " €/t, " + tyonimike.toString() + ", " + tyoaika + " t/pv.");
	}
	
	public String toString()
	{
		return nimi.toString();
	}
	
	public double getPalkka()
	{
		return palkka;
	}
	
	public Object getTyoNimi()
	{
		return tyonimike.toString();
	}
	
	public boolean josMyyja()
	{
		return tyonimike.josMyyja();
	}

	public boolean josVartija()
	{
		return tyonimike.josVartija();
	}
}
