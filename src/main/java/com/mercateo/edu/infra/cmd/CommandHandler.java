package com.mercateo.edu.infra.cmd;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.googlecode.gentyref.GenericTypeReflector;
import com.mercateo.edu.infra.base.Command;

import lombok.NonNull;

@Component
public abstract class CommandHandler<C extends Command> implements Function<C, Effects>, ApplicationContextAware {

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		// register myself to the commandBus
		applicationContext.getBean(CommandBus.class).register(getParameterType(), this);
	}

	@SuppressWarnings("unchecked")
	private Class<C> getParameterType() {
		Type myType = getClass();

		// get the parameterized type, recursively resolving type parameters
		Type baseType = GenericTypeReflector.getExactSuperType(myType, CommandHandler.class);

		if (baseType instanceof Class<?>) {
			// raw class, type parameters not known
			// ...
			throw new RuntimeException("broken command handler");
		} else {
			ParameterizedType pBaseType = (ParameterizedType) baseType;
			assert pBaseType.getRawType() == CommandHandler.class; // always
																	// true
			return (Class<C>) pBaseType.getActualTypeArguments()[0];
		}
	}

	@Override
	public abstract Effects apply(C t) throws RuntimeException;
}
