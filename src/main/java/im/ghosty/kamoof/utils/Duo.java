package im.ghosty.kamoof.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Classe utilitaire pour stocker 2 valeurs.
 * @param <A> Premier type
 * @param <B> Second type
 */
@AllArgsConstructor
@Data
public class Duo<A, B> {
	
	private A a;
	private B b;
	
}
