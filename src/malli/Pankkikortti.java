package malli;

import tuki.MersenneTwisterFast;

public class Pankkikortti
{

	private int pinkoodi;
	private Pankkitili pankkitili;

	public Pankkikortti(final int pinkoodi, final Pankkitili tili)
	{
		this.pinkoodi = pinkoodi;
		pankkitili = tili;
	}

	public double getSaldo()
	{
		return pankkitili.getRahamaara();
	}

	public int getTunnusluku()
	{
		return pinkoodi;
	}

	public int setTunnusluku()
	{
		return pinkoodi;
	}

	/**
	 * @param maara Kuinka paljon maksetaan.
	 * @param saajanTili Kenelle annetaan.
	 * @return true jos rahaa riitti.
	 */
	public boolean maksa(final double maara, final Pankkitili saajanTili)
	{
		return pankkitili.siirto(saajanTili, maara);
	}

	/**
	 * @param tili
	 *            Tili mikä yhdistetään korttiin.
	 * @return Pankkikortin satunnaisella PIN koodila.
	 */
	public static Pankkikortti luoPankkikortti(final Pankkitili tili)
	{
		MersenneTwisterFast satunnaisluku = new MersenneTwisterFast();

		// Uusi kortti satunaisella PIN luvulla.
		return new Pankkikortti(satunnaisluku.randInt(9999), tili);
	}

	public boolean tarkistaPIN(int koodi) {
		return (koodi==pinkoodi);
	}
}
