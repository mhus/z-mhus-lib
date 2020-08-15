package de.mhus.lib.annotations.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// from org.osgi.service.component.annotations.Component

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceComponent {
    /**
     * The name of this Component.
     *
     * <p>If not specified, the name of this Component is the fully qualified type name of the class
     * being annotated.
     *
     * @see "The name attribute of the component element of a Component Description."
     */
    String name() default "";

    /**
     * The types under which to register this Component as a service.
     *
     * <p>If no service should be registered, the empty value <code>&#x7B;&#x7D;</code> must be
     * specified.
     *
     * <p>If not specified, the service types for this Component are all the <i>directly</i>
     * implemented interfaces of the class being annotated.
     *
     * @see "The service element of a Component Description."
     */
    Class<?>[] service() default {};

    /**
     * During initialization, all eager top level managers are requested to provide a component
     * instance. Applications can use this request as an indication to start providing their
     * intended functionality.
     *
     * <p>Managers that are lazy, that is, not singleton scope, activation is lazy, or inlined, are
     * activated when they are first asked to provide a component instance. Therefore, even lazy
     * managers can activate during initialization when they happen to be a dependency of another
     * manager that activates its dependencies.
     *
     * <p>Services and service references can also have lazy or eager activation. The eager
     * activation will ensure that all listeners are properly actuated during the corresponding
     * activation. For services, the service object is then also requested at startup.
     *
     * <p>https://docs.osgi.org/specification/osgi.cmpn/7.0.0/service.blueprint.html#i2707929
     */
    boolean eager() default false;

    /**
     * A bean manager has a recipe for the construction and injection of an object value. However,
     * there can be different strategies in constructing its component instance, this strategy is
     * reflected in the scope. The following scopes are architected for this specification:
     *
     * <p>singleton - The bean manager only holds a single component instance. This object is
     * created and set when the bean is activated. Subsequent requests must provide the same
     * instance. Singleton is the default scope. It is usually used for core component instances as
     * well as stateless services.
     *
     * <p>prototype - The object is created and configured anew each time the bean is requested to
     * provide a component instance, that is, every call to getComponentInstance must result in a
     * new component instance. This is usually the only possible scope for stateful objects. All
     * inlined beans are always prototype scope.
     *
     * <p>Implementations can provide additional scope types. However, these types must only be
     * allowed when a defining namespace is included in the definitions and is actually used in the
     * definitions to specify the dependency on this feature.
     *
     * <p>https://docs.osgi.org/specification/osgi.cmpn/7.0.0/service.blueprint.html#i3015704
     */
    boolean singleton() default false;

    /**
     * Properties for this Component.
     *
     * <p>Each property string is specified as {@code "name=value"}. The type of the property value
     * can be specified in the name as {@code name:type=value}. The type must be one of the property
     * types supported by the type attribute of the property element of a Component Description.
     *
     * <p>To specify a property with multiple values, use multiple name, value pairs. For example,
     * {@code "foo=bar", "foo=baz"}.
     *
     * @see "The property element of a Component Description."
     */
    String[] property() default {};

    /**
     * The configuration PIDs for the configuration of this Component.
     *
     * <p>Each value specifies a configuration PID for this Component.
     *
     * <p>If no value is specified, the name of this Component is used as the configuration PID of
     * this Component.
     *
     * <p>A empty string can be used to specify the name of the component as a configuration PID.
     *
     * <p>Tools creating a Component Description from this annotation must replace the special
     * string with the actual name of this Component.
     *
     * @see "The configuration-pid attribute of the component element of a Component Description."
     * @since 1.2
     */
    String[] configurationPid() default "";
}
