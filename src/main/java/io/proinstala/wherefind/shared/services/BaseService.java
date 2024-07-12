package io.proinstala.wherefind.shared.services;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

public class BaseService {
        protected boolean responseJson(HttpServletResponse response, String data)
    {
        response.setContentType("application/json");
        try
        {
            response.getWriter().write(data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
