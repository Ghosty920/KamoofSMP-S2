package cc.ghosty.kamoof.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Duo<A, B> {
	
	private A a;
	private B b;
	
}
