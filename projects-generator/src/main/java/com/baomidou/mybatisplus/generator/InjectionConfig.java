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
package com.baomidou.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 抽象的对外接口
 * </p>
 *
 * @author hubin
 * @since 2016-12-07
 */
@Data
@Accessors(chain = true)
public class InjectionConfig {

    /**
     * 自定义输出文件
     */
    private List<FileOutConfig> fileOutConfigList = new ArrayList<>();

    public InjectionConfig() {

    }
    public InjectionConfig(List<FileOutConfig> fileOutConfigList) {
        this.fileOutConfigList = fileOutConfigList;
    }

}
