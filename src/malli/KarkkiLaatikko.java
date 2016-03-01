package malli;

import java.util.ArrayList;

import tuki.Mat;
import tuki.MersenneTwisterFast;

/**
 * KarkkiLaatikot määritetään uniikeiksi nimien perusteella.
 */
public class KarkkiLaatikko
{
	private String nimi;
	private double perusKilohinta;
	private int grammoja;
	private double kaupanKilohinta;

	public KarkkiLaatikko(final String nimi, final double perusKilohinta, final int grammaa)
	{
		this.nimi = nimi;
		this.perusKilohinta = perusKilohinta;
		this.kaupanKilohinta = perusKilohinta;
		this.grammoja = grammaa;
	}

	/**
	 * @param grammojaMin
	 *            Määrä grammoina.
	 * @param grammojaMax
	 * @return [4.0,20.0] kilohintaisia karkkeja [1000,grammoja] verran.
	 */
	private static KarkkiLaatikko luoKarkkiLaatikko(final int grammojaMin, int grammojaMax)
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

		// [4.0,20.0] €
		double pohjaKilohinta = Mat.pakotaDesimaalit(2, (satunnaisluku.randInt(1600) + 400) / 100.0);

		return new KarkkiLaatikko(Nimi.luoKarkinNimi(), pohjaKilohinta, satunnaisluku.randInt(grammojaMax - grammojaMin) + grammojaMin);
	}

	/**
	 * Luo karkkivaraston missä on n grammaa karkkia.
	 * 
	 * @param valikoima
	 *            Kuinka monta eri karkkilajia.
	 * @param grammojaMin
	 *            Min grammat.
	 * @param grammojaMax
	 *            max grammat.
	 * @return Varaston.
	 */
	public static ArrayList<KarkkiLaatikko> luoKarkkiVarasto(final int valikoima, final int grammojaMin, final int grammojaMax)
	{
		ArrayList<KarkkiLaatikko> tempvarasto = new ArrayList<KarkkiLaatikko>();

		for (int i = 0; i < valikoima; i++)
		{
			tempvarasto.add(luoKarkkiLaatikko(grammojaMin, grammojaMax));
		}
		return tempvarasto;
	}

	public void tulosta()
	{
		System.out.println(nimi + ": " + Logiikka.muotoileDesimaalit(3, grammoja / 1000.0) + " kg (" + Logiikka.muotoileDesimaalit(2, perusKilohinta) + " €/kg)");
	}

	public void tulosta(Tukku tukku)
	{
		System.out.println(nimi + ": " + Logiikka.muotoileDesimaalit(3, grammoja / 1000.0) + " kg (" + Logiikka.muotoileDesimaalit(2, tukku.getKarkinKilohinta(this)) + " €/kg)");
	}

	/**
	 * @param grammaa
	 *            Paljonko otetaan pois.
	 * @return true jos onnistui, false jos ei ollut tarpeeksi tavaraa mistä
	 *         ottaa pois tai tavaraa ei ole enää yhtään jäljellä.
	 */
	public boolean vahenna(final int grammaa)
	{
		if (grammoja > grammaa)
		{
			grammoja -= grammaa;
			return true;
		}
		else if (grammaa == grammoja)
		{
			grammoja = 0;
			return false;
		}
		else
		{
			return false;
		}
	}

	public void lisaaGrammoja(final int grammaa)
	{
		grammoja += grammaa;
	}

	/**
	 * Tekee kopion olemassaolevasta karkista uudella määrällä. Tämä tarvitaan
	 * koska pelaaja ei välttämättä osta tukusta kaikkia yhden lajin karkkia
	 * kerralla.
	 * 
	 * @param grammat
	 *            Uusi karkkimäärä.
	 * @return kopio karkista.
	 */
	public KarkkiLaatikko kopioiUudellaMaaralla(final int grammat)
	{
		return new KarkkiLaatikko(nimi, perusKilohinta, grammat);
	}

	public int getGrammat()
	{
		return grammoja;
	}

	public double getKilohinta()
	{
		return perusKilohinta;
	}

	public String toString()
	{
		return nimi;
	}

	/**
	 * @return Karkkilaatikon painon tekstinä, kiloina ja kolmen desimaalin
	 *         tarkkuudella.
	 */
	public String getKiloina()
	{
		return Logiikka.muotoileDesimaalit(3, grammoja / 1000.0);
	}

	/**
	 * @return pelaajan määrittämän kilohinnan.
	 */
	public double getKaupanKilohinta()
	{
		return kaupanKilohinta;
	}

	/**
	 * @return karkkilaatikon painon doulena, kiloina ja kolmen desimaalin
	 *         tarkkuudella.
	 */
	public double getDoubleKiloina()
	{
		return Mat.pakotaDesimaalit(3, grammoja / 1000.0);
	}

	public void tulosta(final Kauppa kauppa)
	{
		System.out.println(nimi + ": " + Logiikka.muotoileDesimaalit(3, grammoja / 1000.0) + " kg (" + Logiikka.muotoileDesimaalit(2, kauppa.getKarkinKilohinta(this)) + " €/kg)");
	}

	/**
	 * Pelaaja asettaa karkin kilohinnan.
	 * 
	 * @param uusiHinta
	 *            Uusi kilohinta.
	 */
	public void muutaMyyntihinta(double uusiHinta)
	{
		kaupanKilohinta = uusiHinta;
	}

	public double getPohjahinta()
	{
		return perusKilohinta;
	}
}
