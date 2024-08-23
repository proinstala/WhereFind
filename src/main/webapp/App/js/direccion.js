
import { setImageSelected, solicitudPut, resetCamposForm, detectarCambiosFormulario } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from './alertasSweetAlert2.mjs';

$(document).ready(function () {
    const selectProvincia = document.querySelector('#provincia');
    
    solicitudGet("api/provincia/provincias", adminLoadListUsersCallBack, idElement, true);
    
});


function fillSelect(select, datos) {
    
}
