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

public class LambdaUtil {

	public static boolean debugOut = false;
	
	public static String getMethodName(DoubleUnaryOperator lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodName(LongUnaryOperator lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodName(IntUnaryOperator lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodName(ToLongFunction<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodName(ToDoubleFunction<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodName(ToIntFunction<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T, U> String getMethodName(BiPredicate<T, U> lambda) throws NotFoundException {
		return getName(lambda);
	}

	public static String getMethodName(LongPredicate lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodName(DoublePredicate lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodName(IntPredicate lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <R> String getMethodName(DoubleFunction<R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <R> String getMethodName(LongFunction<R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <R> String getMethodName(IntFunction<R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodName(LongSupplier lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodName(IntSupplier lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static String getMethodName(BooleanSupplier lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodName(Predicate<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodName(BinaryOperator<T> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T,U,R> String getMethodName(BiFunction<T,U,R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T,R> String getMethodName(BiConsumer<T,R> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodName(Consumer<T> lambda) throws NotFoundException {
		return getName(lambda);
	}

	public static String getMethodName(Supplier<?> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	public static <T> String getMethodName(Function<T,?> lambda) throws NotFoundException {
		return getName(lambda);
	}
	
	private static String getName(Object lambda) throws NotFoundException {
		try {
			byte[] bc = getByteCodeOf(lambda.getClass());
			if (debugOut)
				System.out.println(de.mhus.lib.core.MString.toHexDump(bc, 20));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bc.length; i++) {
				if (bc[i] < ' ' || bc[i] > 128)
					sb.append('|');
				else
					sb.append((char)bc[i]);
			}
			String[] parts = sb.toString().split("\\|");
			boolean hiddenFound = false;
			boolean ignoreFound = false;
			for (String p : parts) {
				p = p.trim();
				if (p.indexOf("$Hidden;") > 0) {
					hiddenFound = true;
					continue;
				}
				if (hiddenFound) {
					if (!ignoreFound && (
						p.equals("intValue") || p.equals("longValue") || p.equals("doubleValue") ||
						p.equals("booleanValue") || p.equals("floatValue") || p.equals("shortValue") ||
						p.equals("byteValue") || p.equals("charValue")
						)
						) {
						hiddenFound = true;
						continue;
					}
					
					
					if (p.length() > 1 && p.indexOf('/') < 0 && 
							p.indexOf('$') < 0 && 
							p.indexOf('<') < 0 && 
							p.indexOf('(') < 0 &&
							p.indexOf('[') < 0
							)
						return p;
				}
			}
		} catch (IOException e) {
			throw new NotFoundException("method in lambda not found", lambda, e);
		}
		throw new NotFoundException("method in lambda not found", lambda);
	}
	
	private static final Instrumentation instrumentation = ByteBuddyAgent.install();

	static byte[] getByteCodeOf(Class<?> c) throws IOException {
	    ClassFileLocator locator = ClassFileLocator.AgentBased.of(instrumentation, c);
	    TypeDescription.ForLoadedType desc = new TypeDescription.ForLoadedType(c);
	    ClassFileLocator.Resolution resolution = locator.locate(desc.getName());
	    return resolution.resolve();
	}

	
}
