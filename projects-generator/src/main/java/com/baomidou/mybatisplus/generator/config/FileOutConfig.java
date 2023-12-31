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
package com.baomidou.mybatisplus.generator.config;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Function;

/**
 * <p>
 * 输出文件配置
 * </p>
 *
 * @author hubin
 * @since 2017-01-18
 */
@Data
@Accessors(chain = true)
public abstract class FileOutConfig {

    /**
     * 模板
     */
    private String templatePath;

    public FileOutConfig() {
        // to do nothing
    }

    public FileOutConfig(String templatePath) {
        this.templatePath = templatePath;
    }

    public static FileOutConfig create(String templatePath, Function<TableInfo,String> outputFile) {
        FileOutConfig fileOutConfig = new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return outputFile.apply(tableInfo);
            }
        };
        return fileOutConfig;
    }

    /**
     * 输出文件
     */
    public abstract String outputFile(TableInfo tableInfo);


}
