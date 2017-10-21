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

import de.mhus.lib.core.MString;
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
	
	public static String getDoubleUnaryOperatorName(DoubleUnaryOperator lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getLongUnaryOperatorName(LongUnaryOperator lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getIntUnaryOperatorName(IntUnaryOperator lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getToLongFunctionName(ToLongFunction<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getToDoubleFunctionName(ToDoubleFunction<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getToIntFunctionName(ToIntFunction<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T, U> String getBiPredicateName(BiPredicate<T, U> lambda) throws NotFoundException {
		return getName(lambda);
	}

	public static String getLongPredicateName(LongPredicate lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getDoublePredicateName(DoublePredicate lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getIntPredicateName(IntPredicate lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <R> String getDoubleFunctionName(DoubleFunction<R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <R> String getLongFunctionName(LongFunction<R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <R> String getIntFunctionName(IntFunction<R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getLongSupplierName(LongSupplier lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getIntSupplierName(IntSupplier lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getBooleanSupplierName(BooleanSupplier lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getPredicateName(Predicate<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getBinaryOperatorName(BinaryOperator<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T,U,R> String getBiFunctionName(BiFunction<T,U,R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T,R> String getBiConsumerName(BiConsumer<T,R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getConsumerName(Consumer<T> lambda) throws NotFoundException {
		return getName(lambda);
	}

	public static String getSupplierName(Supplier<?> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getFunctionName(Function<T,?> lambda) throws NotFoundException {
		return getName(lambda);
	}
		
	/*
	 * Get and analyze the lambda byte code.
	 */
	private static String getName(Object lambda) throws NotFoundException {
		byte[] bc = null;
		try {
			bc = getByteCodeOf(lambda.getClass());
			if (debugOut)
				System.out.println(MString.toHexDump(bc, 20));
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
			throw new NotFoundException("method in lambda not found", lambda, MString.toHexDump(bc, 20), e);
		}
		throw new NotFoundException("method in lambda not found", MString.toHexDump(bc, 20), lambda);
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
