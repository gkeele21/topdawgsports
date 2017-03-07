package bglib.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BGBaseAction {
    String process(HttpServletRequest request, HttpServletResponse response);
}
