package it.giovi.configuration

import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.util.StringUtils
import java.lang.reflect.Method


class CustomKeyGenerator : KeyGenerator {

    override fun generate(target: Any, method: Method, vararg params: Any): Any {
        return (target.javaClass.simpleName + "_"
                + method.name + "_"
                + StringUtils.arrayToDelimitedString(params, "_"))
    }
}
