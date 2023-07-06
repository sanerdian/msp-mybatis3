/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jnetdata.msp.util.generator.rules;

/**
 * <p>
 * 表字段类型
 * </p>
 *
 * @author hubin
 * @since 2017-01-11
 */
public enum DbColumnType {
    // 基本类型
    BASE_INT("int", null, 1),
    BASE_BOOLEAN("boolean", null, 1),
    BASE_FLOAT("float", null, 3),
    BASE_DOUBLE("double", null, 3),

    // 包装类型
    STRING("String", null, 2),
    LONG("Long", null, 1),
    INTEGER("Integer", null, 1),
    FLOAT("Float", null, 3),
    DOUBLE("Double", null, 3),
    BOOLEAN("Boolean", null, 1),
    BYTE_ARRAY("byte[]", null, 5),
    CHARACTER("Character", null, 5),
    OBJECT("Object", null, 5),
    DATE("Date", "java.util.Date", 4),
    TIME("Time", "java.sql.Time", 4),
    BLOB("Blob", "java.sql.Blob", 5),
    CLOB("Clob", "java.sql.Clob", 5),
    TIMESTAMP("Timestamp", "java.sql.Timestamp", 4),
    BIG_INTEGER("BigInteger", "java.math.BigInteger", 1),
    BIG_DECIMAL("BigDecimal", "java.math.BigDecimal", 1),
    LOCAL_DATE("LocalDate", "java.time.LocalDate", 4),
    LOCAL_TIME("LocalTime", "java.time.LocalTime", 4),
    LOCAL_DATE_TIME("LocalDateTime", "java.time.LocalDateTime" ,4);

    /**
     * 类型
     */
    private final String type;

    /**
     * 包路径
     */
    private final String pkg;

    /**
     * 包路径
     */
    private final Integer dbType;

    DbColumnType(final String type, final String pkg, final Integer dbType) {
        this.type = type;
        this.pkg = pkg;
        this.dbType = dbType;
    }

    public String getType() {
        return this.type;
    }

    public String getPkg() {
        return this.pkg;
    }

    public Integer getDbType() {
        return this.dbType;
    }
    public Integer getFieldType() {
        return this.dbType==4?4:dbType==5?5:1;
    }

}
