package com.example.redisutil.domain.vo;

import lombok.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JedisLockReq {

    public interface Set {}

    @NotNull(message="keyName cannot be Null", groups = {JedisLockReq.Set.class})
    private String keyName;

    private String value;

    private Integer lockInSecond;

    private Long waitingInMillisecond;

}
