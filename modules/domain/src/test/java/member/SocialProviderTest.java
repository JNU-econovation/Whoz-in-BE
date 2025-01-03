package member;

import com.whoz_in.domain.member.model.SocialProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SocialProviderTest {

    @Test
    @DisplayName("kakao -> KAKAO Enum")
    public void 소셜_제공자_찾기(){
        SocialProvider socialProvider = SocialProvider.findSocialProvider("kakao");

        Assertions.assertEquals(SocialProvider.KAKAO, socialProvider);
    }

}
