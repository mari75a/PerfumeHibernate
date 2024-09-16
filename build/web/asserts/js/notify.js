function success(msg) {
    alert(msg);
//    Swal.fire({
//        title: 'Success!',
//        text: msg,
//        icon: 'success',
//        showConfirmButton: false,
//        toast: true,
//        position: 'top-end',
//        timer: 5000,
//        timerProgressBar: true,
//        showCloseButton: true,        
//        didOpen: (toast) => {
//            toast.onmouseenter = Swal.stopTimer;
//            toast.onmouseleave = Swal.resumeTimer;
//        }
//    })
}
function error(msg) {
     alert(msg);
//    Swal.fire({
//        title: 'Error!',
//        text: msg,
//        icon: 'error',
//        showConfirmButton: false,
//        toast: true,
//        position: 'top-end',
//        timer: 5000,
//        timerProgressBar: true,
//        showCloseButton: true,       
//        didOpen: (toast) => {
//            toast.onmouseenter = Swal.stopTimer;
//            toast.onmouseleave = Swal.resumeTimer;
//        }
//    })
}
function warning(msg) {
    Swal.fire({
        title: 'Warning!',
        text: msg,
        icon: 'warning',
        showConfirmButton: false,
        toast: true,
        position: 'top-end',
        timer: 5000,
        timerProgressBar: true,
        showCloseButton: true,        
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    })
}
function info(msg) {
    Swal.fire({
        title: 'Message!',
        text: msg,
        icon: 'info',
        showConfirmButton: false,
        toast: true,
        position: 'top-end',
        timer: 5000,
        timerProgressBar: true,
        showCloseButton: true,        
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    })
}

