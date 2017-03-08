package tds.playoff.control;

import tds.main.control.BaseView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class rulesView extends BaseView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        return htmlPage;
    }
}