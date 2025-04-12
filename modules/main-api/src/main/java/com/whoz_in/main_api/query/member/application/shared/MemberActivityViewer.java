package com.whoz_in.main_api.query.member.application.shared;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.List;

public interface MemberActivityViewer extends Viewer {
    List<MemberActivityView> findAll();
}
