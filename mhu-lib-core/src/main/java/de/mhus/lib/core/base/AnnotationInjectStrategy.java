package de.mhus.lib.core.base;

import de.mhus.lib.annotations.base.Bind;
import de.mhus.lib.annotations.base.IgnoreBind;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.pojo.AttributesStrategy;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

public class AnnotationInjectStrategy extends InjectStrategy {

	@Override
	public void inject(Object object, Base base) {
		
		IgnoreBind classIgnoreBind = object.getClass().getAnnotation(IgnoreBind.class); // to save time use IgnoreBind ...
		if (classIgnoreBind != null) return;
		
		Bind classBind = MSystem.findAnnotation(object.getClass(), Bind.class);
		if (classBind == null) return;
		
		PojoModel model = new PojoParser().parse(object, new AttributesStrategy(false, false, null, new Class[] {Bind.class})).filter(new DefaultFilter(true,true,false,true,false)).getModel();
		for (PojoAttribute<Object> attr : model) {
			try {
				Bind bind = attr.getAnnotation(Bind.class);
				if (bind != null) {
					Class<?> ifc = attr.getType();
					if (bind.name() != Class.class)
						ifc = bind.name();
					Object obj = base.base(ifc);
						attr.set(object, obj);
				}
			} catch (Throwable e) {
				e.printStackTrace(); //TODO
			}
		}
	}

}
