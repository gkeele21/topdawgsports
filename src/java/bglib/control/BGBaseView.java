package bglib.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BGBaseView {
    String process(HttpServletRequest request, HttpServletResponse response);
}
