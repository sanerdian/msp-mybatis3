package com.jnetdata.msp.manage.publish.core.common.utils;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jnetdata.msp.manage.publish.core.common.utils.parser.ConvertorLookUp;
import com.jnetdata.msp.manage.publish.core.common.utils.parser.DefaultConvertorLookUp;
import com.jnetdata.msp.manage.publish.core.common.utils.parser.SerializeConverter;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.BreadcrumbTagAttr;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Created by xujian on 2017/10/21.
 */
public class BeetlUtils {
    private final static ConvertorLookUp CONVERTOR_LOOK_UP = new DefaultConvertorLookUp();

    public static String detectWebRootPath() {
        try {
            String path = BeetlUtils.class.getResource("/").toURI().getPath();
            return new File(path).getParentFile().getParentFile().getCanonicalPath()+File.separator+ "CMSDATA";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(T obj, Map map) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }

                if (!map.containsKey(field.getName())) {
                    continue;
                }
                final Optional<SerializeConverter> converterOptional = CONVERTOR_LOOK_UP.lookUp(field);
                Preconditions.checkState(converterOptional.isPresent(), "could not convert field type[%s]", field.getType());
                final Object value = converterOptional.get().fromString(field, String.valueOf(map.get(field.getName())));
                FieldUtils.writeField(field, obj, value, true);
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
