package br.com.eduard.storage.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StorageReference {

    boolean mapKey() default true;
    boolean mapValue() default false;
}
