import { solicitudGet } from './comunes.mjs';


const adminLoadListUsers = (idDestino, callBack)  => {
    solicitudGet("api/identidad/users", callBack);
}


export { adminLoadListUsers };
