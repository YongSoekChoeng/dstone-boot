package net.dstone.sample.kakao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class KakaoLoginVo {

    public String accessToken;
    public String refreshToken;
}
