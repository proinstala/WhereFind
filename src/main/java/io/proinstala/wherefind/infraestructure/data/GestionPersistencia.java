package io.proinstala.wherefind.infraestructure.data;

import io.proinstala.wherefind.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.infraestructure.data.services.UserServiceMySql;

public class GestionPersistencia {
    public static IUserService getUserService() {
        return new UserServiceMySql();
    }
}
