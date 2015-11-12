package me.qyh.helper.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignCache {

	String cacheName() default "signCache";

	abstract String cacheKey();

	String condition() default "true";

	int periodSec() default 120;

	int hits() default 5;

}
