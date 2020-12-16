document.addEventListener("DOMContentLoaded", function () {

    $(".audiohive-player audio").on("play", function () {
        const player = $(this);
        if (!player.data('played')) {

            player.data('played', true);

            const form = player.parent();
            $.ajax(form.attr('action'), {
                method: form.attr('method'),
                data: form.serialize(),
            });
        }
    });

})
