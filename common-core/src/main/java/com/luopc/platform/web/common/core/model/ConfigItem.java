package com.luopc.platform.web.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigItem {
    private String key;
    private String value;
    private Boolean isEncrypted;
}
