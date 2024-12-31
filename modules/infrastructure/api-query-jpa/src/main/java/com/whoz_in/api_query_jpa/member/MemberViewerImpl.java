package com.whoz_in.api_query_jpa.member;

import com.whoz_in.main_api.query.member.application.MemberAuthInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberViewerImpl implements MemberViewer {

    private final JdbcClient jdbcClient;

    @Override
    public Optional<MemberAuthInfo> findAuthInfoByLoginId(String loginId) {
        String sql = "select id, login_id, password from member_entity where login_id = ?";

        return jdbcClient.sql(sql)
                .param(loginId)
                .query((rs, rowNum) -> {
                    byte[] idBlob = rs.getBytes("id");
                    UUID id = convertBlobToUUID(idBlob);

                    return new MemberAuthInfo(
                            id,
                            rs.getString("login_id"),
                            rs.getString("password")
                    );
                }).optional();
    }

    private UUID convertBlobToUUID(byte[] blob) {
        ByteBuffer buffer = ByteBuffer.wrap(blob);
        long mostSigBits = buffer.getLong();
        long leastSigBits = buffer.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }
}
