import com.ydo4ki.vird.Vird;
import com.ydo4ki.vird.lang.LangValidationException;

/**
 * @since 7/5/2025 7:54 PM
 * 
 */
public class Playground {
	public static void main(String[] args) throws LangValidationException {
		
		String vird = "(echo 'Hello World') (echo (currentEnv))".replace('\'', '\"');
		Vird.run(vird);
	}
}
