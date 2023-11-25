// Please see documentation at https://docs.microsoft.com/aspnet/core/client-side/bundling-and-minification
// for details on configuring this project to bundle and minify static web assets.

// Write your JavaScript code.

/* Navbar scroll */
$(function () {

    var nav = $('.navbar'),
        doc = $(document),
        win = $(window);

    win.scroll(function () {

        if (doc.scrollTop() > 80) {
            nav.addClass('scrolled');
        } else {
            nav.removeClass('scrolled');
        }

    });

    win.scroll();

});


/* ***** Btn More-Less ***** */
$("#more").click(function () {
    var $this = $(this);
    $this.toggleClass('more');
    if ($this.hasClass('more')) {
        $this.text('More');
    } else {
        $this.text('Less');
    }
});




/* ***** Slideanim  ***** */
$(window).scroll(function () {
    $(".slideanim").each(function () {
        var pos = $(this).offset().top;

        var winTop = $(window).scrollTop();
        if (pos < winTop + 600) {
            $(this).addClass("slide");
        }
    });
});




/* ***** Smooth Scrolling  ***** */
$(document).ready(function () {
    $(".navbar a, #service a").on('click', function (event) {

        if (this.hash !== "") {
            event.preventDefault();
            var hash = this.hash;

            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 900, function () {

                window.location.hash = hash;
            });
        }
    });


    /* ***** Scroll to Top ***** */
    $(window).scroll(function () {
        if ($(this).scrollTop() >= 300) {
            $('.to-top').fadeIn(200);
        } else {
            $('.to-top').fadeOut(200);
        }
    });
    $('.to-top').click(function () {
        $('.body,html').animate({
            scrollTop: 0
        }, 500);
    });

})


$(document).ready(function () {

    var readURL = function (input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('.profile-pic').attr('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
        }
    }

    $(".file-upload").on('change', function () {
        readURL(this);
    });

    $(".upload-button").on('click', function () {
        $(".file-upload").click();
    });
});


let passwordInput = document.getElementById('txtPassword'),
    toggle = document.getElementById('btnToggle'),
    icon = document.getElementById('eyeIcon');

function togglePassword() {
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.classList.add("fa-eye-slash");
        //toggle.innerHTML = 'hide';
    } else {
        passwordInput.type = 'password';
        icon.classList.remove("fa-eye-slash");
        //toggle.innerHTML = 'show';
    }
}

function checkInput() {
    //if (passwordInput.value === '') {
    //toggle.style.display = 'none';
    //toggle.innerHTML = 'show';
    //  passwordInput.type = 'password';
    //} else {
    //  toggle.style.display = 'block';
    //}
}

toggle.addEventListener('click', togglePassword, false);
passwordInput.addEventListener('keyup', checkInput, false);