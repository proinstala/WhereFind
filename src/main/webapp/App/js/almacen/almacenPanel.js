
const idBtnCancelar = "#btnCancelar";

document.addEventListener("DOMContentLoaded", function () {
    
    const btnCancelar = document.querySelector(idBtnCancelar);
    btnCancelar.addEventListener('click', () => {
        window.location.href = "dashboard";
    });
    
});

