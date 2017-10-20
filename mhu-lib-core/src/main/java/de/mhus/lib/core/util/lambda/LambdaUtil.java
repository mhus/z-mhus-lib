package de.mhus.lib.core.util.lambda;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import de.mhus.lib.errors.NotFoundException;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;

/**
 * Utility to help with lambda expressions. The main goal is to find the name of referenced method.
 * 
 * @author mikehummel
 *
 */
public class LambdaUtil {

	public static boolean debugOut = false;
	
	public static String getMethodNameUnaryOperator(DoubleUnaryOperator lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodNameUnaryOperator(LongUnaryOperator lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodNameUnaryOperator(IntUnaryOperator lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodNameToLongFunction(ToLongFunction<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodNameToDoubleFunction(ToDoubleFunction<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodNameToIntFunction(ToIntFunction<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T, U> String getMethodNameBiPredicate(BiPredicate<T, U> lambda) throws NotFoundException {
		return getName(lambda);
	}

	public static String getMethodNameLongPredicate(LongPredicate lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodNameDoublePredicate(DoublePredicate lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodNameIntPredicate(IntPredicate lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <R> String getMethodNameDoubleFunction(DoubleFunction<R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <R> String getMethodNameLongFunction(LongFunction<R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <R> String getMethodNameIntFunction(IntFunction<R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodNameLongSupplier(LongSupplier lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodIntName(IntSupplier lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodNameBooleanSupplier(BooleanSupplier lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodNamePredicate(Predicate<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodNameOperator(BinaryOperator<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T,U,R> String getMethodNameBiFunction(BiFunction<T,U,R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T,R> String getMethodNameBiConsumer(BiConsumer<T,R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodNameConsumer(Consumer<T> lambda) throws NotFoundException {
		return getName(lambda);
	}

	public static String getMethodNameSupplier(Supplier<?> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodName(Function<T,?> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	/*
	 * Get and analyze the lambda byte code.
	 */
	private static String getName(Object lambda) throws NotFoundException {
		try {
			byte[] bc = getByteCodeOf(lambda.getClass());
			if (debugOut)
				System.out.println(de.mhus.lib.core.MString.toHexDump(bc, 20));
			// split the byte code
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bc.length; i++) {
				if (bc[i] < ' ' || bc[i] > 128)
					sb.append('|');
				else
					sb.append((char)bc[i]);
			}
			String[] parts = sb.toString().split("\\|");
			// search for a valid candidate
			boolean hiddenFound = false;
			boolean ignoreFound = false;
			for (String p : parts) {
				p = p.trim();
				if (p.indexOf("$Hidden;") > 0) { // wait for the first 'Hidden'
					hiddenFound = true;
					continue;
				}
				if (hiddenFound) {
					if (!ignoreFound && (
						// ignore primitives for the first time
						p.equals("intValue") || p.equals("longValue") || p.equals("doubleValue") ||
						p.equals("booleanValue") || p.equals("floatValue") || p.equals("shortValue") ||
						p.equals("byteValue") || p.equals("charValue")
						)
						) {
						hiddenFound = true;
						continue;
					}
					
					// check for unwanted characters
					if (p.length() > 1 && p.indexOf('/') < 0 && 
							p.indexOf('$') < 0 && 
							p.indexOf('<') < 0 && 
							p.indexOf('(') < 0 &&
							p.indexOf('[') < 0
							)
						return p; // not ... found it!
				}
			}
		} catch (IOException e) {
			throw new NotFoundException("method in lambda not found", lambda, e);
		}
		throw new NotFoundException("method in lambda not found", lambda);
	}
	
	private static final Instrumentation instrumentation = ByteBuddyAgent.install();
	/*
	 * Use byte buddy to get the lambda byte code
	 */
	static byte[] getByteCodeOf(Class<?> c) throws IOException {
	    ClassFileLocator locator = ClassFileLocator.AgentBased.of(instrumentation, c);
	    TypeDescription.ForLoadedType desc = new TypeDescription.ForLoadedType(c);
	    ClassFileLocator.Resolution resolution = locator.locate(desc.getName());
	    return resolution.resolve();
	}

	
}
