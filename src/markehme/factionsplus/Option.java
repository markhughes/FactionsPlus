package markehme.factionsplus;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.FIELD} )
public @interface Option {

	String comment() default "";

	String[] oldAliases();
	
	boolean[] sub();
	
}
