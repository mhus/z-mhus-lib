/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.vaadin.container;
/*
 * Copyright 2000-2013 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.AbstractProperty;
import com.vaadin.v7.data.util.NestedMethodProperty;
import com.vaadin.v7.data.util.PropertysetItem;
import com.vaadin.v7.data.util.VaadinPropertyDescriptor;

import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

/**
 * A wrapper class for adding the Item interface to any Java Bean.
 * 
 * @author Vaadin Ltd.
 * @param <BT> 
 * @since 3.0
 */
@SuppressWarnings({"serial","deprecation"})
public class MhuBeanItem<BT> extends PropertysetItem {

    /**
     * The bean which this Item is based on.
     */
    private final BT bean;

    /**
     * <p>
     * Creates a new instance of <code>BeanItem</code> and adds all properties
     * of a Java Bean to it. The properties are identified by their respective
     * bean names.
     * </p>
     * 
     * <p>
     * Note : This version only supports introspectable bean properties and
     * their getter and setter methods. Stand-alone <code>is</code> and
     * <code>are</code> methods are not supported.
     * </p>
     * 
     * @param bean
     *            the Java Bean to copy properties from.
     * 
     */
    @SuppressWarnings("unchecked")
	public MhuBeanItem(BT bean) {
        this(bean, getPropertyDescriptors((Class<BT>) bean.getClass()));
    }

    /**
     * <p>
     * Creates a new instance of <code>BeanItem</code> using a pre-computed set
     * of properties. The properties are identified by their respective bean
     * names.
     * </p>
     * 
     * @param bean
     *            the Java Bean to copy properties from.
     * @param propertyDescriptors
     *            pre-computed property descriptors
     */
    MhuBeanItem(BT bean,
            Map<String, PojoPropertyDescriptor<BT>> propertyDescriptors) {

        this.bean = bean;

        for (VaadinPropertyDescriptor<BT> pd : propertyDescriptors.values()) {
            addItemProperty(pd.getName(), pd.createProperty(bean));
        }
    }

    /**
     * <p>
     * Creates a new instance of <code>BeanItem</code> and adds all listed
     * properties of a Java Bean to it - in specified order. The properties are
     * identified by their respective bean names.
     * </p>
     * 
     * <p>
     * Note : This version only supports introspectable bean properties and
     * their getter and setter methods. Stand-alone <code>is</code> and
     * <code>are</code> methods are not supported.
     * </p>
     * 
     * @param bean
     *            the Java Bean to copy properties from.
     * @param propertyIds
     *            id of the property.
     */
    public MhuBeanItem(BT bean, Collection<?> propertyIds) {

        this.bean = bean;

        // Create bean information
        @SuppressWarnings("unchecked")
		LinkedHashMap<String, PojoPropertyDescriptor<BT>> pds = getPropertyDescriptors((Class<BT>) bean
                .getClass());

        // Add all the bean properties as MethodProperties to this Item
        for (Object id : propertyIds) {
            VaadinPropertyDescriptor<BT> pd = pds.get(id);
            if (pd != null) {
                addItemProperty(pd.getName(), pd.createProperty(bean));
            }
        }

    }

    /**
     * <p>
     * Creates a new instance of <code>BeanItem</code> and adds all listed
     * properties of a Java Bean to it - in specified order. The properties are
     * identified by their respective bean names.
     * </p>
     * 
     * <p>
     * Note : This version only supports introspectable bean properties and
     * their getter and setter methods. Stand-alone <code>is</code> and
     * <code>are</code> methods are not supported.
     * </p>
     * 
     * @param bean
     *            the Java Bean to copy properties from.
     * @param propertyIds
     *            ids of the properties.
     */
    public MhuBeanItem(BT bean, String... propertyIds) {
        this(bean, Arrays.asList(propertyIds));
    }

    /**
     * <p>
     * Perform introspection on a Java Bean class to find its properties.
     * </p>
     * 
     * <p>
     * Note : This version only supports introspectable bean properties and
     * their getter and setter methods. Stand-alone <code>is</code> and
     * <code>are</code> methods are not supported.
     * </p>
     * 
     * @param beanClass
     *            the Java Bean class to get properties for.
     * @return an ordered map from property names to property descriptors
     */
    static <BT> LinkedHashMap<String, PojoPropertyDescriptor<BT>> getPropertyDescriptors(
            final Class<BT> beanClass) {
    	
        final LinkedHashMap<String, PojoPropertyDescriptor<BT>> pdMap = new LinkedHashMap<String, PojoPropertyDescriptor<BT>>();

        PojoModel m = new PojoParser().parse(beanClass).getModel();
        
        for (String name : m.getAttributeNames()) {
        	PojoAttribute<?> attr = m.getAttribute(name);
        	pdMap.put(name, new PojoPropertyDescriptor<BT>(attr));
        }
            

        return pdMap;
    }

    public static class PojoPropertyDescriptor<BT> implements VaadinPropertyDescriptor<BT> {

    	
		private PojoAttribute<?> attr;

		public PojoPropertyDescriptor(PojoAttribute<?> attr) {
			this.attr = attr;
		}

		@Override
		public String getName() {
			return attr.getName();
		}

		@Override
		public Class<?> getPropertyType() {
			return attr.getType();
		}

		@Override
		public Property<?> createProperty(BT bean) {
			return new PojoProperty<Object>(attr,bean);
		}
		
		public PojoAttribute<?> getPojoAttribute() {
			return attr;
		}
    	
    }
    
    public static class PojoProperty<T> extends AbstractProperty<T> {

		private Object bean;
		@SuppressWarnings("rawtypes")
		private PojoAttribute attr;

		public PojoProperty(@SuppressWarnings("rawtypes") PojoAttribute attr, Object bean) {
			this.attr = attr;
			this.bean = bean;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T getValue() {
			try {
				return (T) attr.get(bean);
			} catch (IOException e) {
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValue(T newValue)
				throws com.vaadin.v7.data.Property.ReadOnlyException {
			try {
				attr.set(bean, newValue);
			} catch (IOException e) {
				throw new com.vaadin.v7.data.Property.ReadOnlyException("Can't write " + attr.getName() +": " + e);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public Class<? extends T> getType() {
			return attr.getType();
		}
    	
    }
    
    /**
     * Returns the property descriptors of a class or an interface.
     * 
     * For an interface, superinterfaces are also iterated as Introspector does
     * not take them into account (Oracle Java bug 4275879), but in that case,
     * both the setter and the getter for a property must be in the same
     * interface and should not be overridden in subinterfaces for the discovery
     * to work correctly.
     * 
     * For interfaces, the iteration is depth first and the properties of
     * superinterfaces are returned before those of their subinterfaces.
     * 
     * @param beanClass
     * @return
     * @throws IntrospectionException
     */
    @SuppressWarnings("unused")
	private static List<PropertyDescriptor> getBeanPropertyDescriptor(
            final Class<?> beanClass) throws IntrospectionException {
        // Oracle bug 4275879: Introspector does not consider superinterfaces of
        // an interface
        if (beanClass.isInterface()) {
            List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();

            for (Class<?> cls : beanClass.getInterfaces()) {
                propertyDescriptors.addAll(getBeanPropertyDescriptor(cls));
            }

            BeanInfo info = Introspector.getBeanInfo(beanClass);
            propertyDescriptors.addAll(Arrays.asList(info
                    .getPropertyDescriptors()));

            return propertyDescriptors;
        } else {
            BeanInfo info = Introspector.getBeanInfo(beanClass);
            return Arrays.asList(info.getPropertyDescriptors());
        }
    }

    /**
     * Expands nested bean properties by replacing a top-level property with
     * some or all of its sub-properties. The expansion is not recursive.
     * 
     * @param propertyId
     *            property id for the property whose sub-properties are to be
     *            expanded,
     * @param subPropertyIds
     *            sub-properties to expand, all sub-properties are expanded if
     *            not specified
     */
    public void expandProperty(String propertyId, String... subPropertyIds) {
        Set<String> subPropertySet = new HashSet<String>(
                Arrays.asList(subPropertyIds));

        if (0 == subPropertyIds.length) {
            // Enumerate all sub-properties
            Class<?> propertyType = getItemProperty(propertyId).getType();
            Map<String, ?> pds = getPropertyDescriptors(propertyType);
            subPropertySet.addAll(pds.keySet());
        }

        for (String subproperty : subPropertySet) {
            String qualifiedPropertyId = propertyId + "." + subproperty;
            addNestedProperty(qualifiedPropertyId);
        }

        removeItemProperty(propertyId);
    }

    /**
     * Adds a nested property to the item.
     * 
     * @param nestedPropertyId
     *            property id to add. This property must not exist in the item
     *            already and must of of form "field1.field2" where field2 is a
     *            field in the object referenced to by field1
     */
    public void addNestedProperty(String nestedPropertyId) {
        addItemProperty(nestedPropertyId, new NestedMethodProperty<Object>(
                getBean(), nestedPropertyId));
    }

    /**
     * Gets the underlying JavaBean object.
     * 
     * @return the bean object.
     */
    public BT getBean() {
        return bean;
    }

}
