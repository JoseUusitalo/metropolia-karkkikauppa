package tuki;

/**
 * Oma matikkaluokka.
 */
public class Mat
{
	/**
	 * Vääntää luvun väkipakolla Stringin kautta annettuun määrään desimaaleja. Ei pyöristysvirhettä.
	 * 
	 * @param desimaaleja
	 *            Desimaalien määrä. [0,4]
	 * @param luku
	 *            Double luku.
	 * @return luku pyöristettynä annettuun määrään desimaaleja.
	 */
	public static double pakotaDesimaalit(final int desimaaleja, double luku)
	{
		return Double.valueOf(malli.Logiikka.muotoileDesimaalit(desimaaleja, luku));
	}
}
