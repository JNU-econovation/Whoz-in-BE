package com.whoz_in.main_api.query.feedback.presentation;

import com.whoz_in.main_api.query.feedback.view.FeedbackInfo;
import com.whoz_in.main_api.query.feedback.view.FeedbackViewer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedbackViewer viewer;

    @GetMapping("/admin/feedbacks/view")
    public String getFeedback(Model model) {
        List<FeedbackInfo> feedbackList = viewer.findAll();
        model.addAttribute("feedbackList", feedbackList);
        return "feedback";
    }

}
