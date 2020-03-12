/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.pojo;

import java.lang.annotation.Annotation;

public class DefaultStrategy implements PojoStrategy {

    AttributesStrategy attributeStrategy;
    FunctionsStrategy functionsStrategy;

    public DefaultStrategy() {
        this(true, ".", null);
    }

    public DefaultStrategy(
            boolean embedded, String embedGlue, Class<? extends Annotation>[] annotationMarker) {
        attributeStrategy = new AttributesStrategy(embedded, true, embedGlue, annotationMarker);
        functionsStrategy =
                new FunctionsStrategy(embedded, true, embedGlue, false, annotationMarker);
    }

    @Override
    public void parseObject(PojoParser parser, Object pojo, PojoModelImpl model) {
        Class<?> clazz = pojo.getClass();
        parse(parser, clazz, model);
    }

    @Override
    public void parse(PojoParser parser, Class<?> clazz, PojoModelImpl model) {
        functionsStrategy.parse(parser, clazz, model);
        attributeStrategy.parse(parser, clazz, model);
    }

    public boolean isAllowPublic() {
        return functionsStrategy.isAllowPublic();
    }

    public DefaultStrategy setAllowPublic(boolean allowPublic) {
        functionsStrategy.setAllowPublic(allowPublic);
        attributeStrategy.setAllowPublic(allowPublic);
        return this;
    }
}
