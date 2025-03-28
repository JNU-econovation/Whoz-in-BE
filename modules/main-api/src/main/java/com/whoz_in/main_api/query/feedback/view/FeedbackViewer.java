package com.whoz_in.main_api.query.feedback.view;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.List;

public interface FeedbackViewer extends Viewer {

    List<FeedbackInfo> findAll();

}
