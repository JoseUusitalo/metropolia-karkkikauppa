package malli;

import java.util.ArrayList;

import tuki.MersenneTwisterFast;

public class Nimi
{
	private String etunimi;
	private String sukunimi;
	private static String[] etu = { "Eino", "Jere", "Jaakko", "Santeri", "Matti", "Roni", "Tuomas", "Aleksi", "Aapo", "Toni", "Martti", "Veli", "Aku", "Pyry",
			"Vihtori", "Matias", "Kaapo", "Pekka", "Janne", "Sakari", "Emma", "Maija", "Julia", "Kaarina", "Noora", "Olivia", "Eerika", "Hannele", "Amanda",
			"Ester", "Eeva", "Outi", "Maarit", "Tuulia", "Ella", "Ronja", "Venla", "Karoliina", "Olivia", "Nea" };

	private static String[] suku = { "Korhonen", "Virtanen", "Mäkinen", "Nieminen", "Mäkelä", "Hämäläinen", "Laine", "Heikkinen", "Koskinen", "Järvinen",
			"Lehtonen", "Lehtinen", "Saarinen", "Salminen", "Heinonen", "Niemi", "Heikkilä", "Salonen", "Kinnunen", "Turunen", "Salo", "Laitinen", "Tuominen",
			"Rantanen", "Karjalainen", "Jokinen", "Mattila", "Savolainen", "Lahtinen", "Ahonen", "Ojala", "Leppänen", "Hiltunen", "Väisänen", "Leinonen",
			"Miettinen", "Kallio", "Aaltonen", "Pitkänen", "Manninen" };

	private static String[] karkkilista = { "Mansikkaremmi", "Ruskea yllätys", "Kilipallot", "Majavan hampaat", "Possun saparo", "Sininen pilleri", "Netbeans",
			"Villen villit viinerit", "Kopukola", "Karkkimakkara", "Vadelmavene", "Javakarkki", "Tuubi", "Winhat willet", "Binääriaakkoset",
			"Mestarin Mysteerikonvehti", "Ketun nenät", "Kaljaasin rommirusina", "Jallulakritsi", "Kossukonvehti", "Merkkari", "Hulvaton pyörremyrsky",
			"Kodittoman koirakarkki", "Kesän jäätelökarkki", "Syksyn helmi", "Talven lumipallo", "Simpukkanamu", "Javalakritsi", "Audioaakkoset",
			"Ainutlaatuinen elämyskarkki", "Sinivalkoinen suomalainen suklaa", "Joulun taikaa", "Puheen porinaa", "Pikselimössö", "Maukas makrilli",
			"Kesäloman maistiaiset", "Koodarin hiki", "Ekarkki", "Tokarkki", "Mestarin Erikoinen", "Mustasuklaa", "Bulen bullat",
			"Wanhanajan Winhat Wiinerit", };

	private static String[] karkkikauppa = { "Kirkonkulman karkkikauppa", "Karkkikauppa", "Isoroobertinkadun Herkku", "Karkkitalo", "Kievarin karkkilakko",
			"Minun Makuuni", "Keravan karkkikoju", "Karkkikeidas" };

	private static ArrayList<String> kaytetytKarkit = new ArrayList<String>();

	public Nimi(String etu, String suku)
	{
		etunimi = etu;
		sukunimi = suku;
	}

	public String toString()
	{
		return (etunimi + " " + sukunimi);
	}

	/**
	 * @return satunnaisen henkilön nimen.
	 */
	public static Nimi luoHenkilonNimi()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

		String etun = etu[satunnaisluku.randInt(etu.length - 1)];
		String sukun = suku[satunnaisluku.randInt(suku.length - 1)];

		return new Nimi(etun, sukun);
	}

	public static String luoKalusteenNimi()
	{
		return "kaluste";
	}

	/**
	 * @return satunnainen uniikki karkin nimi.
	 */
	public static String luoKarkinNimi()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

		String karkki = karkkilista[satunnaisluku.randInt(karkkilista.length - 1)];

		if (kaytetytKarkit.size() <= karkkilista.length)
		{
			while (kaytetytKarkit.contains(karkki.toString()))
			{
				karkki = karkkilista[satunnaisluku.randInt(karkkilista.length - 1)];
			}
		}

		kaytetytKarkit.add(karkki.toString());

		return karkki;
	}

	public static String luoKaupanNimi()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();
		String kauppa = karkkikauppa[satunnaisluku.randInt(karkkikauppa.length - 1)];
		return kauppa;
	}

	public static Nimi luoKayttajanNimi()
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();
		Nimi nimi[] = { new Nimi("Kaarina", "Kinuski"), new Nimi("Kari", "Kaakao") };
		Nimi kayttaja = nimi[satunnaisluku.randInt(nimi.length - 1)];
		return kayttaja;
	}

	public static int getKarkkiListaKoko()
	{
		return karkkilista.length;
	}

	public static ArrayList<String> getKaytetytKarkit()
	{
		return kaytetytKarkit;
	}
}
