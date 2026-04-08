package com.liyiwei.lanhai.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "lanhai.auth")
public class UserProperties {

    private List<String> noAuthUrls;
}
