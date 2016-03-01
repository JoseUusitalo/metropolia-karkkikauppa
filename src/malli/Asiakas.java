package malli;

import java.util.ArrayList;

/**
 * Asiakkaat ostavat kaupasta karkkia.
 */
public class Asiakas
{
	private Nimi nimi;
	private Pankkitili tili;
	private Pankkikortti kortti;
	private ArrayList<KarkkiLaatikko> karkkisakki;
	private boolean rosvo;

	public Asiakas(final Pankkitili tili, final Pankkikortti kortti)
	{
		nimi = Nimi.luoHenkilonNimi();
		this.tili = tili;
		this.kortti = kortti;
		rosvo = false;
	}

	public Asiakas(final boolean luoRosvo)
	{
		if (luoRosvo)
		{
			tili = new Pankkitili(0);
			kortti = Pankkikortti.luoPankkikortti(tili);
			nimi = new Nimi("Rosvo", "");
			karkkisakki = new ArrayList<KarkkiLaatikko>();
			rosvo = true;
		}
	}

	public Pankkitili getTili()
	{
		return tili;
	}

	public String toString()
	{
		return nimi.toString();
	}

	public Pankkikortti getKortti()
	{
		return kortti;
	}

	public double getRaha()
	{
		return kortti.getSaldo();
	}

	/**
	 * @return satunnaisen asiakkaan.
	 */
	public static Asiakas luoAsiakas()
	{
		Pankkitili tili = Pankkitili.luoPankkitili();
		Pankkikortti kortti = Pankkikortti.luoPankkikortti(tili);

		return new Asiakas(tili, kortti);
	}

	/**
	 * @return true jos tavaraa jäljellä, false liian vähän.
	 */
	public boolean osta()
	{
		if (Logiikka.kauppa.asiakasOsto(this))
			return true;
		else
			return false;
	}

	/**
	 * @param hinta
	 *            Eurot.
	 * @return true jos asiakkaalla on varaa ostaa tuote.
	 */
	public boolean tarpeeksiRahaa(double hinta)
	{
		return (hinta <= getRaha());
	}

	public boolean josRosvo()
	{
		return rosvo;
	}

	/**
	 * Tyhjentää asiakkaan tilin.
	 * 
	 * @return Tilillä olevat rahat.
	 */
	public double tyhjennaTili()
	{
		double raha = tili.getRahamaara();
		tili.poistaTililta(raha);
		return raha;
	}

	public ArrayList<KarkkiLaatikko> getKarkkiSakki()
	{
		return karkkisakki;
	}

	/**
	 * Lisää karkin varkaan säkkiin.
	 * @param varastettuKarkki Karkkilaatikko.
	 */
	public void varasta(KarkkiLaatikko varastettuKarkki)
	{
		karkkisakki.add(varastettuKarkki);
	}
}
